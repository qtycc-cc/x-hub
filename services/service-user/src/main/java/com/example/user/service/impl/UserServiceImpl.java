package com.example.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.example.model.entity.User;
import com.example.model.exception.BusinessException;
import com.example.model.exception.MyIllegalArgumentException;
import com.example.model.request.UserCommonRequest;
import com.example.model.response.R;
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
    public R<User> login(UserCommonRequest userCommonRequest) {
        if (userCommonRequest == null ||
            userCommonRequest.getAccount() == null ||
            userCommonRequest.getPassword() == null) {
            throw new MyIllegalArgumentException("Account and password are required!!!");
        }
        User user = userMapper.selectByAccountAndPassword(userCommonRequest);
        if (user == null) {
            throw new BusinessException("Account or password is incorrect!!!");
        }
        return R.ok("User login success", user);
    }

    @Override
    public R<Integer> register(UserCommonRequest userCommonRequest) {
        User user = new User();
        user.setId(MyIdGenerator.generateId(idConfig.getWorkerId(), idConfig.getDatacenterId()));
        user.setAccount(userCommonRequest.getAccount());
        user.setPassword(DigestUtils.md5DigestAsHex(userCommonRequest.getPassword().getBytes()));
        user.setApiKey(null);
        Integer affectedRows = userMapper.insert(user);
        return R.ok("User register success", affectedRows);
    }
}
