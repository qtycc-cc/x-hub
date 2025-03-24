package com.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.entity.User;
import com.example.model.request.UserCommonRequest;

@Mapper
public interface UserMapper {
    User selectById(Long id);
    User selectByAccountAndPassword(UserCommonRequest userCommonRequest);
    Integer insert(User user);
}
