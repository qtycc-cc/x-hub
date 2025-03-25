package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;

/**
 * [Sa-Token 权限认证] 配置类
 *
 * @author click33
 */
@Configuration
public class SaTokenConfigure {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**") /* 拦截全部path */
                // 开放地址
                .addExclude("/favicon.ico",
                        "/api/user/login/**",
                        "/api/user/register/**",
                        "/api/user/logout/**",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/swagger-config",
                        "/v3/api-docs/**",
                        "/swagger-ui/index.html",
                        "swagger-ui.html")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(),
                            StpUtil.getTokenValue());
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    return SaResult.error(e.getMessage());
                });
    }
}
