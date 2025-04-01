package com.blog.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Component
public class CustomTokenUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Access token 유효기간 (예: 30분)
    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000L;  // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;  // 7일

    // Access Token 생성
    public String generateAccessToken(Long userId, String email) {
        return generateToken(userId, email, ACCESS_TOKEN_EXPIRATION);
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long userId, String email) {
        return generateToken(userId, email,REFRESH_TOKEN_EXPIRATION);
    }

    // 공통 토큰 생성 메서드
    public String generateToken(Long userId, String email, long expirationTime) {
        long now = System.currentTimeMillis();
        long expiration = now + expirationTime;

        // Header (HMAC SHA256 방식으로 서명할 예정)
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = String.format("{\"userId\":\"%d\", \"email\":\"%s\", \"iat\":%d, \"exp\":%d}",
                userId, email, now, expiration);

        // Header와 Payload를 Base64로 인코딩
        String encodedHeader = Base64.getEncoder().withoutPadding().encodeToString(header.getBytes());
        String encodedPayload = Base64.getEncoder().withoutPadding().encodeToString(payload.getBytes());

        // 서명 생성
        String signature = generateSignature(encodedHeader + "." + encodedPayload);
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    // 토큰 검증
    public static boolean validateToken(String token) {
        try {
            // 1. 토큰이 3부분 (헤더, 페이로드, 서명) 으로 나누어지는지 확인
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false; // 잘못된 토큰 형식
            }
            // 헤더 + 페이로드로 서명을 생성하고 검증
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            // 2. 서명이 올바른지 검증 (서명 검증)
            String signatureToVerify = generateSignature(header + "." + payload);
            if (!signatureToVerify.equals(signature)) {
                return false; // 서명 불일치
            }

            // 3. 페이로드에서 만료 시간 추출 및 유효 기간 검증
            // JSON 파싱하여 만료 시간 확인
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));
            Map<String, Object> payloadMap = objectMapper.readValue(decodedPayload, Map.class);


            long expiration = (long) payloadMap.get("exp");
            return System.currentTimeMillis() < expiration;

        } catch (Exception e) {
            return false;
        }
    }


    // 토큰에서 ID와 Email을 추출
    public Map<String, Object> getUserFromToken(String token) {
        System.out.println("getUserFromToken");
        try {
            String[] parts = token.split("\\.");

            // Base64 디코딩
            String decodedPayload = new String(Base64.getUrlDecoder().decode(parts[1]));
            System.out.println("Decoded Payload: " + decodedPayload);


            String userId = decodedPayload.split("\"userId\":\"")[1].split("\"")[0];
            String email = decodedPayload.split("\"email\":\"")[1].split("\"")[0];

            System.out.println("User ID: " + userId);
            System.out.println("Email: " + email);

            // 반환값: userId와 email을 포함한 Map
            Map<String, Object> user = new HashMap<>();
            user.put("userId", userId);
            user.put("email", email);

            return user;

        } catch (Exception e) {
            System.err.println("토큰 처리 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        return null;}
    }

    // HMAC SHA-256 서명 생성
    private static String generateSignature(String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            String mySecretKey = "your-secret-key-here";
            SecretKeySpec secretKey = new SecretKeySpec(mySecretKey.getBytes(), "HmacSHA256");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash); // Base64 인코딩 추가
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC", e);
        }
    }






}
