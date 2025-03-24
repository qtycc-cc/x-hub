package com.example.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class User {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String account;
    private String password;
    private String apiKey;
}
