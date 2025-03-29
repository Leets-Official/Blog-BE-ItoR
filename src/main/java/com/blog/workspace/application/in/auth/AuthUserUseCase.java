package com.blog.workspace.application.in.auth;

import com.blog.workspace.adapter.in.web.dto.request.AuthFirstTimeRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import com.blog.workspace.application.out.auth.OAuthLoginParams;
import com.blog.workspace.domain.user.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthUserUseCase {

    /*
        사용자는 카카오 OAuth를 통해 회원가입을 진행할 수 있어야 합니다.
        카카오 OAuth를 통해 이메일, 이미지, 이름을 얻어올 수 있습니다.
     */

    // 소셜 로그인 (회원 가입 혹은 로그인 기능 수행)
    UserLoginResponse login(HttpServletResponse httpServletResponse, OAuthLoginParams params);

    // 최초 가입 유저 추가정보 기입
    User updateInfo(String email, AuthFirstTimeRequest request);

}
