package com.example.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chat.mapper.ChatMapper;
import com.example.chat.service.ChatService;
import com.example.model.entity.ChatMeta;
import com.example.model.request.UserChatRequest;
import com.example.model.response.R;

import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;

    @Override
    public R<List<ChatMeta>> getChatMetasByUserId(Long userId) {
        List<ChatMeta> chatMetas = chatMapper.selectChatMetasByUserId(userId);
        return R.ok("Find success", chatMetas);
    }

    @Override
    public Flux<String> chat(UserChatRequest userChatRequest) {
        return null;
    }
}
