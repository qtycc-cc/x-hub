package com.example.chat.service;

import com.example.model.response.R;

import java.util.List;

import com.example.model.entity.ChatMeta;

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
}
