package com.example.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.request.UserCommonRequest;
import com.example.model.response.R;
import com.example.model.response.SimpleResponse;
import com.example.model.response.UserCommonResponse;
import com.example.user.service.UserService;

import cn.dev33.satoken.stp.StpUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<R<SimpleResponse>> logout() {
        StpUtil.logout();
        SimpleResponse response = new SimpleResponse();
        response.setMessage("Logout success!!!");
        return ResponseEntity.ok(R.ok("User logout success", response));
    }

    @PostMapping("/login")
    public ResponseEntity<R<UserCommonResponse>> login(@RequestBody UserCommonRequest userCommonRequest) {
        return ResponseEntity.ok(userService.login(userCommonRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<R<SimpleResponse>> register(@RequestBody UserCommonRequest userCommonRequest) {
        return ResponseEntity.ok(userService.register(userCommonRequest));
    }
}
