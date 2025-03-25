package com.example.model.response;

import com.example.model.type.ChatRespType;
import com.example.model.type.ModelType;

import lombok.Data;

@Data
public class UserChatResponse {
    private ChatRespType type;
    private Integer id;
    private String topic;
    private String data;
    private ModelType model;
}
