package com.blog.domain.token.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.blog.domain.token.domain.RefreshToken;
import com.blog.domain.token.repository.RefreshTokenRepository;
import com.blog.global.auth.jwt.JwtUtil;

@Service
public class TokenService {

	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	public TokenService(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
		this.jwtUtil = jwtUtil;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public void issueRefreshToken(int userId) throws Exception {
		String token = jwtUtil.createRefreshToken(String.valueOf(userId));
		LocalDateTime exp = LocalDateTime.now().plusDays(7);

		RefreshToken refreshToken = RefreshToken.create(userId, token, exp);
		refreshTokenRepository.save(refreshToken);
	}
}
