package com.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.entity.User;
import com.example.model.request.UserCommonRequest;

@Mapper
public interface UserMapper {
    User selectById(Long id);
    User selectByAccount(String account);
    User selectByAccountAndPassword(UserCommonRequest userCommonRequest);
    Integer insert(User user);
    Integer update(User user);
}
