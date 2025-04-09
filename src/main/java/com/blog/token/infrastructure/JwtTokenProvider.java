package com.blog.token.infrastructure;

import com.blog.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long accessTokenExpireMillis = 1000 * 60 * 15; // 15분
    private final long refreshTokenExpireMillis = 1000 * 60 * 60 * 24 * 7; // 7일

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String createAccessToken(User user) {
        long now = System.currentTimeMillis();
        long exp = now + accessTokenExpireMillis;

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = String.format(
                "{\"sub\":\"%s\",\"email\":\"%s\",\"iat\":%d,\"exp\":%d}",
                user.getUserId(), user.getEmail(), now / 1000, exp / 1000
        );

        return buildJwt(headerJson, payloadJson);
    }

    public String createRefreshToken(User user) {
        long now = System.currentTimeMillis();
        long exp = now + refreshTokenExpireMillis;

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = String.format(
                "{\"sub\":\"%s\",\"type\":\"refresh\",\"iat\":%d,\"exp\":%d}",
                user.getUserId(), now / 1000, exp / 1000
        );

        return buildJwt(headerJson, payloadJson);
    }

    public Timestamp getRefreshTokenExpiry() {
        return new Timestamp(System.currentTimeMillis() + refreshTokenExpireMillis);
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            // 1. 서명 검증
            String data = header + "." + payload;
            String expectedSignature = createHmacSha256Signature(data);
            if (!expectedSignature.equals(signature)) return false;

            // 2. 만료시간 검증
            String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            long exp = extractExpFromPayload(payloadJson);

            long now = System.currentTimeMillis() / 1000;
            return now < exp;
        } catch (Exception e) {
            return false;
        }
    }

    private long extractExpFromPayload(String payloadJson) {
        // 단순 문자열 파싱 (json parser 없이)
        // {"sub":"4","email":"...","iat":...,"exp":...}
        String[] parts = payloadJson.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split(",");
        for (String part : parts) {
            if (part.trim().startsWith("exp:")) {
                return Long.parseLong(part.split(":")[1].trim());
            }
        }
        throw new RuntimeException("exp 필드가 없음");
    }

    private String buildJwt(String headerJson, String payloadJson) {
        String header = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signature = createHmacSha256Signature(header + "." + payload);

        return String.format("%s.%s.%s", header, payload, signature);
    }

    private String createHmacSha256Signature(String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(keySpec);
            byte[] signatureBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncode(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create signature", e);
        }
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String parseUserId(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new RuntimeException("JWT 구조가 잘못됨");

        String payload = parts[1];
        String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);

        String[] entries = payloadJson.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split(",");

        for (String entry : entries) {
            if (entry.trim().startsWith("sub:")) {
                return entry.split(":")[1].trim();
            }
        }

        throw new RuntimeException("sub 필드가 없음");
    }

    public Integer extractUserId(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Integer.parseInt(parseUserId(token));
    }


}
