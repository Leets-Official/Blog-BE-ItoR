package com.blog.workspace.application.in.user;

import com.blog.workspace.adapter.in.web.dto.request.UserLoginRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginUseCase {

    /*
        사용자는 등록한 이메일 주소 또는 카카오 로그인을 이용하여 로그인할 수 있어야 합니다.
        (토큰 방식으로 구현시) refresh token을 통해 새로운 access token을 발급받을 수 있어야 합니다.
     */

    // 토큰과 유저 정보를 같이 넘기기 위해 DTO 반환
    UserLoginResponse login(HttpServletResponse response, UserLoginRequest request);

}
