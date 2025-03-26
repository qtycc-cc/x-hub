package com.example.chat.pojo;

import java.util.List;

import com.example.model.type.ModelType;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;

import lombok.Data;

@Data
public class UserChatState {
    private Long currentId;
    private List<ChatMessage> chatMessages;
    private String topic;
    private ModelType model;
    private boolean starred;
}
