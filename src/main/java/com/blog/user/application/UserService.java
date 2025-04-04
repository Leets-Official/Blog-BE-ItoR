package com.blog.user.application;

import com.blog.user.presentation.dto.UserSignupRequest;
import com.blog.user.presentation.dto.UserResponse;

//회원가입 전용 인터페이스
public interface UserService {
    UserResponse signup(UserSignupRequest request);
}
