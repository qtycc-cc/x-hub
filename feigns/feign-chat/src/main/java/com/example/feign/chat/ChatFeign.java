package com.example.feign.chat;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.model.entity.ChatMeta;
import com.example.model.response.R;

@FeignClient(name = "service-chat")
public interface ChatFeign {
    @GetMapping("/chatmeta/{userId}")
    ResponseEntity<R<List<ChatMeta>>> getChatMetas(@PathVariable Long userId);
}
