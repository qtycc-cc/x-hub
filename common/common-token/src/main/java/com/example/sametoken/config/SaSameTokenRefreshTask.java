package com.example.sametoken.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import cn.dev33.satoken.same.SaSameUtil;

/**
 * Same-Token，定时刷新
 */
@Configuration
public class SaSameTokenRefreshTask {
    // 从 0 分钟开始 每隔 5 分钟执行一次 Same-Token
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshToken() {
        SaSameUtil.refreshToken();
    }
}
