package com.example.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chat.mapper.ChatMapper;
import com.example.chat.service.ChatService;
import com.example.model.entity.ChatMeta;
import com.example.model.response.R;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;

    @Override
    public R<List<ChatMeta>> getChatMetasByUserId(Long userId) {
        List<ChatMeta> chatMetas = chatMapper.selectChatMetasByUserId(userId);
        return R.ok("Find success", chatMetas);
    }
}
