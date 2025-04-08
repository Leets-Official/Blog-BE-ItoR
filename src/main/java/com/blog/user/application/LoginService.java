package com.blog.user.application;

import com.blog.token.presentation.dto.TokenResponse;

//로그인 전용 인터페이스
public interface LoginService {
    TokenResponse login(Object request);   // EmailLoginRequest, KakaoLoginRequest 등 모두 수용
    String getLoginType();                 // "email", "kakao", ...
}
