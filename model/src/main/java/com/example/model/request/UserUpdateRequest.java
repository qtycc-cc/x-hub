package com.example.model.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String password;
    private String apiKey;
}
