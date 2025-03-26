package com.example.chat.service;

import com.example.model.response.R;

import reactor.core.publisher.Flux;

import java.util.List;

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
     * Chat
     * @param userChatRequest user chat request see {@link UserChatRequest}
     * @return chat flux
     */
    Flux<String> chat(UserChatRequest userChatRequest);
    /**
     * Get chat metas by key word
     * @param keyWord used to get chat metas
     * @return chat metas see {@link ChatMeta}
     */
    R<List<ChatMeta>> getChatMetasByKeyWord(String keyWord);
}
