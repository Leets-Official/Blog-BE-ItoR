package com.blog.workspace.application.in.token;

import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenUseCase {

    // 리프레시 토큰을 바탕으로 재발급
    UserLoginResponse getAccessTokenByRefreshToken(HttpServletRequest httpServletRequest);

    // 토큰에서 UserId 얻기
    Long getUserIdFromToken(HttpServletRequest httpServletRequest);
}
