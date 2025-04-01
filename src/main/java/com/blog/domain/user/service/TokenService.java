package com.blog.domain.user.service;

import com.blog.domain.user.repository.TokenStore;
import com.blog.global.security.CustomTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenService {
    
    private final CustomTokenUtil customTokenUtil;
    private final TokenStore tokenStore;

    public TokenService(CustomTokenUtil customTokenUtil, TokenStore tokenStore) {
        this.customTokenUtil = customTokenUtil;
        this.tokenStore = tokenStore;
    }

    // access 토큰 생성
    public String generateAccessToken(Long userId, String email) {
        return customTokenUtil.generateAccessToken(userId, email);
    }

    // refresh 토큰 생성
    public String generateRefreshToken(Long userId, String email) {
        String refreshToken=customTokenUtil.generateRefreshToken(userId, email);
        tokenStore.storeToken(userId,refreshToken);
        return String.valueOf(tokenStore.getToken(userId));
    }

    // 토큰을 요청에서 추출하는 방법
    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


    // 쿠키에서 userId 가져오기
    public Long getUserIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    return Long.valueOf(cookie.getValue());
                }
            }
        }

        return null;
    }

    //  쿠키에 UserId 추가하기
    public void addCookie(String userId, HttpServletResponse response) {
        Cookie userIdCookie = new Cookie("userId", userId);
        userIdCookie.setMaxAge(60 * 60);
        response.addCookie(userIdCookie);
    }


    public Map<String, Object> decodeUserFromToken(String refreshToken) {
        return customTokenUtil.getUserFromToken(refreshToken);
    }
}
