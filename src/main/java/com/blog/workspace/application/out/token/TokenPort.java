package com.blog.workspace.application.out.token;

import com.blog.workspace.domain.token.JwtToken;
import com.blog.workspace.domain.user.User;

import java.util.Optional;

public interface TokenPort {

    // Refresh 토큰 저장
    JwtToken saveToken(JwtToken jwtToken);

    // 유저 정보 가져오기
    Optional<Long> getUserByToken(String refreshToken);
}
