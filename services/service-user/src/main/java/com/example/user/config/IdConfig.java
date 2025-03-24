package com.example.user.config;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class IdConfig {
    private Long workerId = 1L;
    private Long datacenterId = 1L;
}
