package com.example.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.entity.ChatMeta;

@Mapper
public interface ChatMapper {
    List<ChatMeta> selectChatMetasByUserId(Long userId);
}
