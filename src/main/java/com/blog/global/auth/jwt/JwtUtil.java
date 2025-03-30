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
		long now = System.currentTimeMillis();
		long exp = now + expiration;

		// 1. Header
		String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
		String encodedHeader = base64UrlEncode(headerJson);

		// 2. Payload
		String payloadJson = String.format("{\"sub\":\"%s\",\"exp\":%d}", userId, exp);
		String encodedPayload = base64UrlEncode(payloadJson);

		// 3. Signature
		String message = encodedHeader + "." + encodedPayload;
		String signature = hmacSha256(message, secretKey);

		// 4. Token 조립
		return message + "." + signature;
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
			// 만료 시간 직접 비교 (payload에서 exp 추출)
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

	private String extractValueFromJson(String json, String key) {
		String search = "\"" + key + "\":";
		int start = json.indexOf(search);
		if (start == -1) return null;

		start += search.length();
		int end = json.indexOf(",", start);
		if (end == -1) end = json.indexOf("}", start);
		if (end == -1) return null;

		String value = json.substring(start, end).replaceAll("[\"}]", "").trim();
		return value;
	}
}
