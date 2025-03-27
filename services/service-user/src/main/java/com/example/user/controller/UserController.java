package com.example.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.request.UserCommonRequest;
import com.example.model.request.UserUpdateRequest;
import com.example.model.response.R;
import com.example.model.response.SimpleResponse;
import com.example.model.response.UserCommonResponse;
import com.example.user.service.UserService;

import cn.dev33.satoken.stp.StpUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/logout")
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

    @GetMapping("/user")
    public ResponseEntity<R<UserCommonResponse>> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PatchMapping("/user")
    public ResponseEntity<R<SimpleResponse>> updateUserInfo(@RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUserInfo(userUpdateRequest));
    }
}
