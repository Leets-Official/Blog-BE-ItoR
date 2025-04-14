package com.blog.global.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expiration;

	public String createToken(String userId) throws Exception {
		return generateToken(userId, expiration);
	}

	public String createRefreshToken(String userId) throws Exception {
		long refreshExp = 1000L * 60 * 60 * 24 * 7; // 7일
		return generateToken(userId, refreshExp);
	}

	private String generateToken(String userId, long expireMillis) throws Exception {
		long now = System.currentTimeMillis();
		long exp = now + expireMillis;

		String header = base64UrlEncode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
		String payload = base64UrlEncode(String.format("{\"sub\":\"%s\",\"exp\":%d}", userId, exp));
		String signature = hmacSha256(header + "." + payload, secretKey);

		return header + "." + payload + "." + signature;
	}

	private String base64UrlEncode(String data) {
		return Base64.getUrlEncoder().withoutPadding()
			.encodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

	private String hmacSha256(String data, String key) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		sha256_HMAC.init(secretKeySpec);
		byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
		return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
	}

	public boolean validateToken(String token) {
		try {
			String[] parts = token.split("\\.");
			if (parts.length != 3) return false;

			String payloadJson = new String(Base64.getDecoder().decode(parts[1]));
			String expStr = extractValueFromJson(payloadJson, "exp");

			if (expStr == null) return false;

			long exp = Long.parseLong(expStr);
			long now = System.currentTimeMillis() / 1000;

			return now < exp;
		} catch (Exception e) {
			return false;
		}
	}

	public String extractUserId(String token) {
		try {
			String[] parts = token.split("\\.");
			if (parts.length != 3) return null;

			String payloadJson = new String(Base64.getDecoder().decode(parts[1]));
			return extractValueFromJson(payloadJson, "sub");
		} catch (Exception e) {
			return null;
		}
	}

	public String parseUserIdFromBearerToken(String bearerToken) {
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) return null;
		String rawToken = bearerToken.substring(7);
		return extractUserId(rawToken);
	}


	private String extractValueFromJson(String json, String key) {
		String search = "\"" + key + "\":";
		int start = json.indexOf(search);
		if (start == -1) return null;

		start += search.length();
		int end = json.indexOf(",", start);
		if (end == -1) end = json.indexOf("}", start);
		if (end == -1) return null;

		return json.substring(start, end).replaceAll("[\"}]", "").trim();
	}
}
