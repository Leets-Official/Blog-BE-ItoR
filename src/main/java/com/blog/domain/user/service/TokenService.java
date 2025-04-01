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
        String refreshTokenStore= String.valueOf(tokenStore.getToken(userId));
        System.out.println("refreshTokenStore"+refreshTokenStore);

        System.out.println("refresh: "+refreshToken);
        return refreshToken;
    }

    // 토큰을 요청에서 추출하는 방법
    public String getTokenFromRequest(HttpServletRequest request) {
        // 예시로 Authorization 헤더에서 'Bearer <token>' 형식으로 토큰을 가져온다고 가정
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // "Bearer " 부분 제거 후 토큰 반환
        }
        return null;
    }

    // 토큰에서 사용자 ID를 추출하는 로직 (예: JWT를 파싱하여 사용자 ID를 추출)
    public String getUserIdFromToken(String token) {
        // JWT 토큰을 파싱하여 사용자 ID를 추출하는 로직 구현
        // 여기서는 단순히 토큰을 문자열로 처리하는 예시입니다.
        return token.split("\\.")[0];  // 토큰의 첫 번째 부분을 사용자 ID로 가정
    }


//    public String refreshAccessToken(String refreshToken) {
//        if (CustomTokenUtil.validateToken(refreshToken)) {
//            Map<String, Object> userInfo = customTokenUtil.getUserFromToken(refreshToken);
//            return customTokenUtil.generateAccessToken((Long) userInfo.get("userId"), (String) userInfo.get("email"));
//        } else {
//            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//    }


    //  쿠키에서 사용자 정보 추출
    // 쿠키에서 userId 가져오기
    public Long getUserIdFromCookie(HttpServletRequest request) {
        // 쿠키 배열을 가져옵니다.
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // userId 쿠키가 존재하는지 확인
                if ("userId".equals(cookie.getName())) {
                    return Long.valueOf(cookie.getValue());  // userId 값 반환
                }
            }
        }

        return null;  // userId 쿠키가 없으면 null 반환
    }

    //  쿠키에 UserId 추가하기
    public void addCookie(String userId, HttpServletResponse response) {
        Cookie userIdCookie = new Cookie("userId", userId);
        userIdCookie.setMaxAge(60 * 60);
        response.addCookie(userIdCookie);
    }

    // 쿠키에서 Refresh Token 가져오기
//    private String getRefreshTokenFromCookies(HttpServletRequest request) {
//        if (request.getCookies() == null) return null;
//
//        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
//                .filter(cookie -> "refresh_token".equals(cookie.getName()))
//                .findFirst();
//
//        return refreshTokenCookie.map(Cookie::getValue).orElse(null);
//    }


    public Map<String, Object> decodeUserFromToken(String refreshToken) {
        return customTokenUtil.getUserFromToken(refreshToken);
    }
}
