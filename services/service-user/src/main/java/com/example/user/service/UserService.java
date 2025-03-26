package com.example.user.service;

import com.example.model.entity.User;
import com.example.model.request.UserCommonRequest;
import com.example.model.response.R;
import com.example.model.response.SimpleResponse;
import com.example.model.response.UserCommonResponse;

/**
 * UserService
 */
public interface UserService {
    /**
     * login
     * @param userCommonRequest see {@link UserCommonRequest}
     * @return logined User see {@link User}
     */
    R<UserCommonResponse> login(UserCommonRequest userCommonRequest);
    /**
     * register
     * @param userCommonRequest see {@link UserCommonRequest}
     * @return SimpleResponse see {@link SimpleResponse}
     */
    R<SimpleResponse> register(UserCommonRequest userCommonRequest);
}
