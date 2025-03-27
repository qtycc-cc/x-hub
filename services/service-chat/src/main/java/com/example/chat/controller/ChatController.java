package com.example.chat.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.model.response.R;
import com.example.model.response.SimpleResponse;

import reactor.core.publisher.Flux;

import com.example.chat.service.ChatService;
import com.example.model.entity.Chat;
import com.example.model.entity.ChatMeta;
import com.example.model.request.UserChatRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/chatmeta/{userId}")
    public ResponseEntity<R<List<ChatMeta>>> getChatMetasByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getChatMetasByUserId(userId));
    }

    // @GetMapping("/chat/{userId}")
    // public ResponseEntity<R<List<Chat>>> getChatsByUserId(@PathVariable Long userId) {
    //     return ResponseEntity.ok(chatService.getChatsByUserId(userId));
    // }

    @GetMapping("/chat/{id}")
    public ResponseEntity<R<Chat>> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }

    @GetMapping("/chatmeta/query")
    public ResponseEntity<R<List<ChatMeta>>> getChatMetasByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(chatService.getChatMetasByKeyword(keyword));
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody UserChatRequest userChatRequest) {
        return chatService.chat(userChatRequest);
    }

    @PostMapping("/star/{id}")
    public ResponseEntity<R<SimpleResponse>> star(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.star(id));
    }

    @DeleteMapping("/star/{id}")
    public ResponseEntity<R<SimpleResponse>> unstar(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.unstar(id));
    }
}
