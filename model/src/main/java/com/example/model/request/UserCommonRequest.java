package com.example.model.request;

import lombok.Data;

/**
 * 用户登录注册请求
 */
@Data
public class UserCommonRequest {
    private String account;
    private String password;
}
