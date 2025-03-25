package com.example.model.request;

import com.example.model.type.ModelType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserChatRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String message;
    private ModelType model;
    private String prompt;
}
