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

}
