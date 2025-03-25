package com.example.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserChatRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String message;
    private String model;
    private String prompt;
}
