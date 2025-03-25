package com.example.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import com.example.model.entity.ChatMeta;

@Data
public class UserCommonResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String account;
    private String apiKey;
    private List<ChatMeta> chatMetas;
}
