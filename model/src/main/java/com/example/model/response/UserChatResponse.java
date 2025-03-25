package com.example.model.response;

import com.example.model.type.ChatRespType;
import com.example.model.type.ModelType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChatResponse {
    private ChatRespType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String topic;
    private String data;
    private ModelType model;
}
