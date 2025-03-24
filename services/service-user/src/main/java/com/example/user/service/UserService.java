package com.example.user.service;

import com.example.model.entity.User;
import com.example.model.request.UserCommonRequest;

/**
 * UserService
 */
public interface UserService {
    /**
     * login
     * @param userCommonRequest see {@link UserCommonRequest}
     * @return logined User see {@link User}
     */
    User login(UserCommonRequest userCommonRequest);
    /**
     * register
     * @param userCommonRequest see {@link UserCommonRequest}
     * @return Integer affected rows
     */
    Integer register(UserCommonRequest userCommonRequest);
}
