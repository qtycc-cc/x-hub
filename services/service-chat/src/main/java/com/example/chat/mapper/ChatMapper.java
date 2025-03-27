package com.example.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.entity.Chat;
import com.example.model.entity.ChatMeta;

@Mapper
public interface ChatMapper {
    List<ChatMeta> selectChatMetasByUserId(Long userId);
    List<Chat> selectChatsByUserId(Long userId);
    Chat selectById(Long id);
    Integer insert(Chat chat);
    Integer starChat(Long id);
    Integer unstarChat(Long id);
    Integer deleteChat(Long id);
}
