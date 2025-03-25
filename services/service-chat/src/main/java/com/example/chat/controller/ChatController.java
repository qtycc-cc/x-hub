package com.example.chat.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.model.response.R;

import reactor.core.publisher.Flux;

import com.example.chat.service.ChatService;
import com.example.model.entity.ChatMeta;
import com.example.model.request.UserChatRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/chatmeta/{userId}")
    public ResponseEntity<R<List<ChatMeta>>> getChatMetas(@PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getChatMetasByUserId(userId));
    }

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody UserChatRequest userChatRequest) {
        return chatService.chat(userChatRequest);
    }
}
