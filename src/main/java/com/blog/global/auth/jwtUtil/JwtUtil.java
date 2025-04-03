package com.blog.global.auth.jwtUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
    private static final String SECRET_KEY = "my-secret-key";
    private static final String ALGORITHM = "HmacSHA256";
    private static final long EXPIRATION_TIME = 3600000;

    public static String generateToken(String userId, String email) {
        long now = System.currentTimeMillis();
        String payload = "{\"id\":\"" + userId + "\",\"email\":\"" + email + "\",\"exp\":" + (now + EXPIRATION_TIME) + "}";
        String header = base64UrlEncode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payloadEncoded = base64UrlEncode(payload);
        String signature = createSignature(header + "." + payloadEncoded);
        return header + "." + payloadEncoded + "." + signature;
    }

    public static boolean validateToken(String token) {
        try {
            Map<String, Object> claims = verifyToken(token);
            long exp = (long) claims.get("exp");
            return exp > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<String, Object> verifyToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new RuntimeException("Invalid token format");

        if (!createSignature(parts[0] + "." + parts[1]).equals(parts[2])) {
            throw new RuntimeException("Invalid token signature");
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        return parsePayload(payloadJson);
    }

    private static String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String createSignature(String data) {
        try {
            Mac hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            hmac.init(keySpec);
            return base64UrlEncode(new String(hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error creating JWT signature", e);
        }
    }

    private static Map<String, Object> parsePayload(String payloadJson) {
        Map<String, Object> claims = new HashMap<>();
        String[] fields = payloadJson.replace("{", "").replace("}", "").replace("\"", "").split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":");
            if (keyValue[0].trim().equals("exp")) {
                claims.put(keyValue[0].trim(), Long.parseLong(keyValue[1].trim()));
            } else {
                claims.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return claims;
    }

    public static Map<String, Object> getUserInfo(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new RuntimeException("Invalid token format");

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        return parsePayload(payloadJson);
    }
}
