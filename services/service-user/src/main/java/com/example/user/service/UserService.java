package com.example.user.service;

import com.example.model.request.UserCommonRequest;
import com.example.model.request.UserUpdateRequest;
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
     * @return logined user info see {@link UserCommonResponse}
     */
    R<UserCommonResponse> login(UserCommonRequest userCommonRequest);
    /**
     * register
     * @param userCommonRequest see {@link UserCommonRequest}
     * @return SimpleResponse see {@link SimpleResponse}
     */
    R<SimpleResponse> register(UserCommonRequest userCommonRequest);
    /**
     * Get user info
     * @return user info {@link UserCommonResponse}
     */
    R<UserCommonResponse> getUserInfo();
    /**
     * <p>Update user info</p>
     * Can only change password and apiKey
     * @param userUpdateRequest
     * @return
     */
    R<SimpleResponse> updateUserInfo(UserUpdateRequest userUpdateRequest);
}
