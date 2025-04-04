package com.blog.security.jwt.provider;

import com.blog.workspace.domain.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/*
    JWT 부분은 GPT의 도움을 받았습니다 ...
 */

@Component
public class JwtTokenProvider {

    @Value("${jwt.key}")
    private String key;  // JWT를 서명할 비밀 키

    // AccessToken의 기본 만료 시간 (1시간)
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1;  // 1시간 (3600초)

    // RefreshToken의 기본 만료 시간 (7일)
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 7;  // 7일 (604800초)

    // JWT 생성 (AccessToken과 RefreshToken을 생성하기 위한 기본 메서드)
    public String createJwt(User user, long expirationTime) {
        long now = System.currentTimeMillis() / 1000;  // 현재 시간 (초 단위)
        long exp = now + expirationTime;  // 만료 시간 (파라미터로 받은 시간 추가)

        // 헤더 부분 (JSON)
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String encodedHeader = Base64.getUrlEncoder().encodeToString(header.getBytes(StandardCharsets.UTF_8));

        // 페이로드 부분 (JSON)
        String payload = String.format("{\"sub\":\"%s\",\"id\":%d,\"iat\":%d,\"exp\":%d}", user.getEmail(), user.getId(), now, exp);
        String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        // 서명 부분 (HMAC SHA256을 사용하여 헤더와 페이로드를 서명)
        String signature = generateSignature(encodedHeader, encodedPayload);

        // 최종 JWT 토큰 생성
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    // Access Token 생성 (만료 시간: 1시간)
    public String createAccessToken(User user) {
        return createJwt(user, ACCESS_TOKEN_EXPIRATION_TIME);  // 상수로 설정된 만료 시간 사용
    }

    // Refresh Token 생성 (만료 시간: 7일)
    public String createRefreshToken(HttpServletResponse response, User user) {

        String refresh = createJwt(user, REFRESH_TOKEN_EXPIRATION_TIME);// 상수로 설정된 만료 시간 사용

        // 쿠키에 저장
        setRefreshTokenInCookie(response, refresh);

        return refresh;
    }

    // 서명 생성
    private String generateSignature(String encodedHeader, String encodedPayload) {
        try {
            String data = encodedHeader + "." + encodedPayload;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");  // key로 서명 생성
            mac.init(secretKeySpec);

            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("서명 생성 실패", e);
        }
    }

    // JWT 유효성 검사 (서명을 체크)
    public boolean validateJwt(String token) {

        // 없는 경우 예외처리
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("RefreshToken이 없습니다.");
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;  // 토큰 형식이 잘못된 경우
        }

        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];

        String expectedSignature = generateSignature(header, payload);
        return expectedSignature.equals(signature);  // 서명이 일치하는지 확인
    }

    // JWT에서 아이디 추출 (페이로드에서)
    public Long getUserIdFromToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }

        // 페이로드 디코딩
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        String[] claims = payload.replace("{", "").replace("}", "").split(",");

        for (String claim : claims) {
            if (claim.startsWith("\"id\":")) {
                return Long.parseLong(claim.split(":")[1].trim());
            }
        }

        return null;
    }

    // 쿠키에 refreshToken 저장하는 메서드
    public void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // JavaScript에서 접근하지 못하도록 설정
        cookie.setSecure(false); // HTTPS에서만 전송되도록 설정
        cookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        cookie.setMaxAge((int) REFRESH_TOKEN_EXPIRATION_TIME); // 7일 동안 유효
        response.addCookie(cookie); // 응답에 쿠키 추가
    }
}
