package com.example.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.entity.User;
import com.example.model.request.UserCommonRequest;
import com.example.model.response.R;
import com.example.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<R<User>> login(@RequestBody UserCommonRequest userCommonRequest) {
        return ResponseEntity.ok(userService.login(userCommonRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<R<Integer>> register(@RequestBody UserCommonRequest userCommonRequest) {
        return ResponseEntity.ok(userService.register(userCommonRequest));
    }
}
