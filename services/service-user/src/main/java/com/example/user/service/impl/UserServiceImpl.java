package com.example.user.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.example.model.entity.User;
import com.example.feign.chat.ChatFeign;
import com.example.model.entity.ChatMeta;
import com.example.model.exception.AccountHasBeenUsedException;
import com.example.model.exception.BusinessException;
import com.example.model.exception.InvalidCredentialsException;
import com.example.model.exception.MyIllegalArgumentException;
import com.example.model.request.UserCommonRequest;
import com.example.model.request.UserUpdateRequest;
import com.example.model.response.R;
import com.example.model.response.SimpleResponse;
import com.example.model.response.UserCommonResponse;
import com.example.user.config.IdConfig;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import com.example.utils.MyIdGenerator;

import cn.dev33.satoken.stp.StpUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdConfig idConfig;
    @Autowired
    private ChatFeign chatFeign;

    @Override
    public R<UserCommonResponse> getUserInfo() {
        User currentUser = (User) StpUtil.getSession().get("currentUser");
        UserCommonResponse userCommonResponse = new UserCommonResponse();
        BeanUtils.copyProperties(currentUser, userCommonResponse);
        R<List<ChatMeta>> chatMetasResponse = chatFeign.getChatMetas(currentUser.getId()).getBody();
        if (chatMetasResponse == null || chatMetasResponse.getData() == null) {
            throw new BusinessException("Get chat metas failed!!!");
        }
        List<ChatMeta> chatMetas = chatMetasResponse.getData();
        userCommonResponse.setChatMetas(chatMetas);
        return R.ok("Get success!!!", userCommonResponse);
    }

    @Override
    public R<SimpleResponse> updateUserInfo(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null) {
            throw new MyIllegalArgumentException("Request can not be null!!!");
        }
        String apiKey = userUpdateRequest.getApiKey();
        String password = userUpdateRequest.getPassword();
        if (apiKey == null && password == null) {
            throw new MyIllegalArgumentException("Must change one!!!");
        }
        User currentUser = (User) StpUtil.getSession().get("currentUser");
        User user = new User();
        user.setId(currentUser.getId());
        if (apiKey != null) {
            user.setApiKey(apiKey);
        }
        if (password != null) {
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        }
        Integer affectedRows = userMapper.update(user);
        if (affectedRows == 0) {
            throw new BusinessException("Update faied!!!");
        }
        SimpleResponse response = new SimpleResponse();
        response.setMessage(String.format("%d rows affected!!!", affectedRows));
        return R.ok("Update success", response);
    }

    @Override
    public R<UserCommonResponse> login(UserCommonRequest userCommonRequest) {
        if (userCommonRequest == null ||
            userCommonRequest.getAccount() == null ||
            userCommonRequest.getPassword() == null) {
            throw new MyIllegalArgumentException("Account and password are required!!!");
        }
        // Encrypt password
        userCommonRequest.setPassword(DigestUtils.md5DigestAsHex(userCommonRequest.getPassword().getBytes()));
        User user = userMapper.selectByAccountAndPassword(userCommonRequest);
        if (user == null) {
            throw new InvalidCredentialsException("Account or password is incorrect!!!");
        }
        UserCommonResponse userCommonResponse = new UserCommonResponse();
        BeanUtils.copyProperties(user, userCommonResponse);
        R<List<ChatMeta>> chatMetasResponse = chatFeign.getChatMetas(user.getId()).getBody();
        if (chatMetasResponse == null || chatMetasResponse.getData() == null) {
            throw new BusinessException("Get chat metas failed!!!");
        }
        List<ChatMeta> chatMetas = chatMetasResponse.getData();
        userCommonResponse.setChatMetas(chatMetas);
        StpUtil.login(user.getId());
        StpUtil.getSession().set("currentUser", user);
        userCommonResponse.setToken(StpUtil.getTokenInfo());
        return R.ok("User login success", userCommonResponse);
    }

    @Override
    public R<SimpleResponse> register(UserCommonRequest userCommonRequest) {
        if (userCommonRequest == null ||
            userCommonRequest.getAccount() == null ||
            userCommonRequest.getPassword() == null) {
            throw new MyIllegalArgumentException("Account and password are required!!!");
        }

        if (userMapper.selectByAccount(userCommonRequest.getAccount()) != null) {
            throw new AccountHasBeenUsedException("Account already exists!!!");
        }
        User user = new User();
        user.setId(MyIdGenerator.generateId(idConfig.getWorkerId(), idConfig.getDatacenterId()));
        user.setAccount(userCommonRequest.getAccount());
        user.setPassword(DigestUtils.md5DigestAsHex(userCommonRequest.getPassword().getBytes()));
        user.setApiKey(null);
        Integer affectedRows = userMapper.insert(user);
        if (affectedRows == 0) {
            throw new BusinessException("User register failed!!!");
        }
        SimpleResponse response = new SimpleResponse();
        response.setMessage(String.format("%d rows affected!!!", affectedRows));
        return R.ok("User register success", response);
    }
}
