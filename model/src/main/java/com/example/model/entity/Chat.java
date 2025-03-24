package com.example.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Chat {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private Long userId;
    private String content;
    private String model;
    private boolean starred;
}
