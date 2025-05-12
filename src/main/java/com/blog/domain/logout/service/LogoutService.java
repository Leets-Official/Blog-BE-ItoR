package com.blog.domain.logout.service;

import com.blog.global.security.jwt.JwtUtil;
import com.blog.global.security.jwt.repository.TokenStore;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private final JwtUtil jwtUtil;
    private final TokenStore tokenStore;

    public LogoutService(JwtUtil jwtUtil, TokenStore tokenStore){
        this.jwtUtil = jwtUtil;
        this.tokenStore = tokenStore;
    }

    public void signOut(String token) {
        int userId = jwtUtil.getUserIdFromToken(token);
        tokenStore.removeRefreshToken(userId);
    }
}
