package com.example.chat.service;

import com.example.model.response.R;
import com.example.model.response.SimpleResponse;

import reactor.core.publisher.Flux;

import java.util.List;

import com.example.model.entity.Chat;
import com.example.model.entity.ChatMeta;
import com.example.model.request.UserChatRequest;

/**
 * ChatService
 */
public interface ChatService {
    /**
     * Get chat metas by user id
     * @param userId user id
     * @return chat metas see {@link ChatMeta}
     */
    R<List<ChatMeta>> getChatMetasByUserId(Long userId);
    /**
     * Get chats by user id
     * @param userId user id
     * @return chat see {@link Chat}
     */
    R<List<Chat>> getChatsByUserId(Long userId);
    /**
     * Chat
     * @param userChatRequest user chat request see {@link UserChatRequest}
     * @return chat flux
     */
    Flux<String> chat(UserChatRequest userChatRequest);
    /**
     * Get chat metas by key word
     * @param keyword used to get chat metas
     * @return chat metas see {@link ChatMeta}
     */
    R<List<ChatMeta>> getChatMetasByKeyword(String keyword);
    /**
     * Get chat by id
     * @param id chat id
     * @return chat see {@link Chat}
     */
    R<Chat> getChatById(Long id);
    /**
     * Star chat
     * @param id chat id
     * @return SimpleResponse see {@link SimpleResponse}
     */
    R<SimpleResponse> star(Long id);
    /**
     * Unstar chat
     * @param id chat id
     * @return SimpleResponse see {@link SimpleResponse}
     */
    R<SimpleResponse> unstar(Long id);
    /**
     * Delete chat
     * @param id chat id
     * @return SimpleResponse see {@link SimpleResponse}
     */
    R<SimpleResponse> deleteChat(Long id);
}
