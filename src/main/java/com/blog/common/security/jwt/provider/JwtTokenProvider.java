package com.blog.common.security.jwt.provider;

import com.blog.workspace.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

    /*
        GPT 기반 JWT...
    */

@Component
public class JwtTokenProvider {

    @Value("${jwt.key}")
    private String key;  // JWT를 서명할 비밀 키

    // JWT 생성
    public String createJwt(User user) {
        long now = System.currentTimeMillis() / 1000;
        long exp = now + 86400;  // 만료 시간 (30 분 후)

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

    // JWT에서 이메일 추출 (페이로드에서)
    public String getEmailFromToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        String[] claims = payload.replace("{", "").replace("}", "").split(",");

        for (String claim : claims) {
            if (claim.startsWith("\"sub\":\"")) {
                return claim.split(":")[1].replace("\"", "");
            }
        }

        return null;
    }
}
