package com.example.chat.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chat.config.IdConfig;
import com.example.chat.mapper.ChatMapper;
import com.example.chat.service.ChatService;
import com.example.model.entity.Chat;
import com.example.model.entity.ChatMeta;
import com.example.model.entity.User;
import com.example.model.exception.BusinessException;
import com.example.model.exception.MyCannotAcquireLockException;
import com.example.model.exception.MyIllegalArgumentException;
import com.example.model.request.UserChatRequest;
import com.example.model.response.R;
import com.example.model.response.UserChatResponse;
import com.example.model.type.ChatRespType;
import com.example.model.type.ModelType;
import com.example.utils.MyIdGenerator;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.reactivex.Flowable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    private ArkService arkService;
    private ArkService topicGenerationService;
    @Autowired
    private IdConfig idConfig;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private Redisson redisson;

    private static final String DEEPSEEK_MODEL = "deepseek-r1-250120";
    private static final String DOUBAO_MODEL = "doubao-1-5-pro-256k-250115";
    private static final String GET_TOPIC_PROMPT = """
            你是一名擅长会话的助理，你需要将用户的会话总结为 10 个字以内的标题，标题语言与用户的首要语言一致，不要使用标点符号和其他特殊符号
            """;
    private static final String REASON_PREFIX = "reason: ";
    private static final String USER_CHAT_STATE_KEY = "chat:state:";
    private static final String LOCK_KEY_SUFFIX = ":lock";

    @Override
    public R<List<ChatMeta>> getChatMetasByUserId(Long userId) {
        List<ChatMeta> chatMetas = chatMapper.selectChatMetasByUserId(userId);
        return R.ok("Find success", chatMetas);
    }

    @Override
    public Flux<String> chat(UserChatRequest userChatRequest) {
        if (userChatRequest == null
                || StrUtil.isBlank(userChatRequest.getMessage())
                || userChatRequest.getModel() == null) {
            throw new MyIllegalArgumentException("Request cannot be empty!!!");
        }

        User currentUser = (User) StpUtil.getSession().get("currentUser");

        String userChatStateKey = USER_CHAT_STATE_KEY + currentUser.getId();
        String lockKey = userChatStateKey + LOCK_KEY_SUFFIX;
        RLock lock = redisson.getLock(lockKey);

        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (!locked) {
                throw new MyCannotAcquireLockException("Acquire lock failed!!!");
            }

            StringBuilder reasonContent = new StringBuilder("");
            StringBuilder assistantContent = new StringBuilder("");

            RBucket<UserChatState> stateBucket = redisson.getBucket(userChatStateKey);
            UserChatState userChatState = stateBucket.get();

            if (userChatState == null) {
                userChatState = new UserChatState();
            }

            if (userChatRequest.getId() == null) {
                // 新聊天
                // 存入数据库
                if (userChatState.getCurrentId() != null) {
                    savePengdingMessages(userChatState, currentUser.getId());
                }
                // 创建会话
                Chat chat = new Chat();
                chat.setId(MyIdGenerator.generateId(idConfig.getWorkerId(), idConfig.getDatacenterId()));
                chat.setModel(userChatRequest.getModel());
                chat.setStarred(false);
                chat.setContent("[]");
                chat.setTopic(getTopic(userChatRequest.getMessage(), currentUser.getApiKey()));
                // 修改userChatState
                userChatState.setCurrentId(chat.getId());
                userChatState.setChatMessages(new CopyOnWriteArrayList<>());
                userChatState.setTopic(chat.getTopic());
                userChatState.setModel(userChatRequest.getModel());
                userChatState.setStarred(false);
            } else if (!userChatState.getCurrentId().equals(userChatRequest.getId())) {
                // 继续聊天
                // 存入数据库
                if (userChatState.getCurrentId() != null) {
                    savePengdingMessages(userChatState, currentUser.getId());
                }
                // 读取数据库
                Chat requiredChat = chatMapper.selectById(userChatRequest.getId());
                // 设置userChatState
                userChatState.setCurrentId(userChatRequest.getId());
                JSONArray jsonArray = JSONUtil.parseArray(requiredChat.getContent());
                userChatState.setChatMessages(JSONUtil.toList(jsonArray, ChatMessage.class));
                userChatState.setTopic(requiredChat.getTopic());
                userChatState.setModel(requiredChat.getModel());
                userChatState.setStarred(requiredChat.isStarred());
            }

            // 构建arkService
            arkService = ArkService.builder().apiKey(currentUser.getApiKey())
                    .timeout(Duration.ofMinutes(30))
                    .build();

            // 提示词
            if (userChatRequest.getPrompt() != null && !StrUtil.isBlank(userChatRequest.getPrompt())) {
                ChatMessage sysMessage = ChatMessage.builder()
                        .role(ChatMessageRole.SYSTEM)
                        .content(userChatRequest.getPrompt())
                        .build();
                userChatState.getChatMessages().add(sysMessage);
            }
            // 创建用户消息
            ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER) // 设置消息角色为用户
                    .content(userChatRequest.getMessage()) // 设置消息内容
                    .build();
            // 将用户消息添加到消息列表
            userChatState.getChatMessages().add(userMessage);
            // 创建聊天完成请求
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(getModel(userChatRequest.getModel()))
                    .messages(userChatState.getChatMessages()) // 设置消息列表
                    .build();
            // 建立流
            Flowable<String> flowableResponse = Flowable
                    .fromPublisher(arkService.streamChatCompletion(chatCompletionRequest))
                    .map(choice -> {
                        if (choice.getChoices().size() > 0) {
                            ChatMessage message = choice.getChoices().get(0).getMessage();
                            String responseContent = (String) message.getContent();
                            // 处理模型输出的内容
                            if (message.getReasoningContent() != null && !message.getReasoningContent().isEmpty()) {
                                responseContent = REASON_PREFIX + message.getReasoningContent(); // 使用推理内容
                                reasonContent.append(message.getReasoningContent());
                            } else {
                                assistantContent.append(message.getContent());
                            }
                            return responseContent;
                        }
                        return "";
                    })
                    .doOnError(Throwable::printStackTrace);

            stateBucket.set(userChatState);

            return Flux.deferContextual(ctx -> {
                UserChatState currentState = (UserChatState)ctx.get("userChatState");
                Long id = currentState.getCurrentId();
                ModelType model = userChatRequest.getModel();
                String topic = currentState.getTopic();
                return Flux.concat(
                    Flux.just(JSONUtil.toJsonStr(
                        UserChatResponse.builder()
                                    .type(ChatRespType.METADATA)
                                    .id(id)
                                    .model(model)
                                    .topic(topic)
                                    .build()
                    )),
                    Flux.from(flowableResponse)
                            .map(content -> JSONUtil.toJsonStr(
                                UserChatResponse.builder()
                                    .type(ChatRespType.MESSAGE)
                                    .data(content)
                                    .build()
                            )),
                    Flux.just(JSONUtil.toJsonStr(
                        UserChatResponse.builder()
                                    .type(ChatRespType.END)
                                    .build()
                    ))
                )
                .doOnComplete(() -> {
                    currentState.getChatMessages().add(ChatMessage.builder()
                                .role(ChatMessageRole.ASSISTANT)
                                .reasoningContent(reasonContent.toString())
                                .build());
                    currentState.getChatMessages().add(ChatMessage.builder()
                                .role(ChatMessageRole.ASSISTANT)
                                .content(assistantContent.toString())
                                .build());
                });
            })
            .contextWrite(Context.of("userChatState", userChatState));

        } catch (InterruptedException e) {
            log.error("InterruptedException :{}", e);
            throw new BusinessException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Data
    private static class UserChatState {
        private Long currentId;
        private List<ChatMessage> chatMessages;
        private String topic;
        private ModelType model;
        private boolean starred;
    }

    private String getModel(ModelType model) {
        String res;
        switch (model) {
            case DEEPSEEK:
                res = DEEPSEEK_MODEL;
                break;
            case DOUBAO:
                res = DOUBAO_MODEL;
                break;
            default:
                res = DEEPSEEK_MODEL;
                break;
        }
        return res;
    }

    private String getTopic(String message, String apiKey) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            List<ChatMessage> messages = new CopyOnWriteArrayList<>();
            ChatMessage sysMessage = ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM)
                    .content(GET_TOPIC_PROMPT)
                    .build();
            messages.add(sysMessage);
            ChatMessage titleMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content(message)
                    .build();
            messages.add(titleMessage);
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(DOUBAO_MODEL) // 使用成本较低的模型
                    .messages(messages)
                    .build();
            topicGenerationService = ArkService.builder().apiKey(apiKey)
                    .timeout(Duration.ofMinutes(30))
                    .build();
            ChatCompletionResult result = topicGenerationService.createChatCompletion(request);
            String content = (String) result.getChoices().get(0).getMessage().getContent();
            return content.replaceAll("\"", "").trim();
        });
        try {
            return future.get(8, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new BusinessException();
        }
    }

    private void savePengdingMessages(UserChatState state, Long userId) {
        Chat chat = new Chat();
        chat.setId(state.getCurrentId());
        chat.setUserId(userId);
        chat.setModel(state.getModel());
        chat.setTopic(state.getTopic());
        chat.setStarred(state.isStarred());
        chat.setContent(JSONUtil.toJsonStr(state.getChatMessages()));
        chatMapper.insert(chat);
    }
}
