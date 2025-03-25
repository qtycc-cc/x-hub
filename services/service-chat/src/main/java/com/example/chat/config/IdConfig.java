package com.example.chat.config;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class IdConfig {
    private Long workerId = 2L;
    private Long datacenterId = 1L;
}
