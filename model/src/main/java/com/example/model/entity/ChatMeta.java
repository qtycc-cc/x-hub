package com.example.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ChatMeta {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private Long userId;
    private String topic;
    private String model;
    private boolean starred;
}
