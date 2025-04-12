package com.blog.domain.user.service;

import com.blog.domain.user.repository.TokenStore;
import com.blog.global.exception.CustomException;
import com.blog.global.security.CustomTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.util.Map;

import static com.blog.global.exception.ErrorCode.USER_NOT_FOUND_IN_COOKIE;

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
    public String generateRefreshToken(Long userId, String email, HttpServletResponse response) {
        String refreshToken=customTokenUtil.generateRefreshToken(userId, email);
        tokenStore.storeToken(userId,refreshToken);

        // 쿠키 넣기
        addCookie(String.valueOf(userId), response);
        return refreshToken;
    }

    // 토큰을 요청에서 추출하는 방법
    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // 재발급 하는 로직
    public String getAccessTokenFromRequest(HttpServletRequest request) {

        // 쿠키에서 사용자 ID 가져오기
        Long userId = getUserIdFromCookie(request);

        String refreshToken = String.valueOf(tokenStore.getToken(userId));
        Map<String, Object> user = decodeUserFromToken(refreshToken);

        Long userIdfromAccessToken = Long.parseLong((String) user.get("userId"));
        String userEmailfromAccessToken = (String) user.get("email");

        return generateAccessToken(userIdfromAccessToken, userEmailfromAccessToken);

    }

    // 로그아웃할때, 토큰을 삭제
    public void deleteRefreshTokenByLogout(HttpServletRequest request) {

        String token = getTokenFromRequest(request);

        // 토큰에서 사용자 정보 추출 (디코딩)
        Map<String, Object> user = decodeUserFromToken(token);

        // userId가 Long 형식이라면
        Long userId = Long.valueOf(user.get("userId").toString());

        tokenStore.removeToken(userId);
    }

    //  쿠키에 UserId 추가하기
    private void addCookie(String userId, HttpServletResponse response) {
        Cookie userIdCookie = new Cookie("userId", userId);
        userIdCookie.setMaxAge(60 * 60);
        userIdCookie.setHttpOnly(true);
        userIdCookie.setPath("/");
        response.addCookie(userIdCookie);
    }


    // 쿠키에서 userId 가져오기
    public Long getUserIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            throw new CustomException(USER_NOT_FOUND_IN_COOKIE);
        }

        for (Cookie cookie : cookies) {
            if ("userId".equals(cookie.getName())) {
                return Long.valueOf(cookie.getValue());
            }
        }

        return null;
    }


    public Map<String, Object> decodeUserFromToken(String refreshToken) {
        return customTokenUtil.getUserFromToken(refreshToken);
    }


}
