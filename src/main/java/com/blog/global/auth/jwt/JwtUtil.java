package com.blog.global.auth.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expiration;

	public String createToken(String userId) {
		Date now = new Date();
		Date expireAt = new Date(now.getTime() + expiration);
	//jjwt 라이브러리 의존성 추가여부 확인 후 수정 예정
		return Jwts.builder()
			.setSubject(userId)
			.setIssuedAt(now)
			.setExpiration(expireAt)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

}
