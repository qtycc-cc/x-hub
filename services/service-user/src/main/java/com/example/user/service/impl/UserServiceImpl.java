package com.example.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.example.model.entity.User;
import com.example.model.request.UserCommonRequest;
import com.example.user.config.IdConfig;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import com.example.utils.MyIdGenerator;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdConfig idConfig;

    @Override
    public User login(UserCommonRequest userCommonRequest) {
        return userMapper.selectByAccountAndPassword(userCommonRequest);
    }

    @Override
    public Integer register(UserCommonRequest userCommonRequest) {
        User user = new User();
        user.setId(MyIdGenerator.generateId(idConfig.getWorkerId(), idConfig.getDatacenterId()));
        user.setAccount(userCommonRequest.getAccount());
        user.setPassword(DigestUtils.md5DigestAsHex(userCommonRequest.getPassword().getBytes()));
        user.setApiKey(null);
        return userMapper.insert(user);
    }
}
