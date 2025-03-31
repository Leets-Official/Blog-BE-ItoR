package com.blog.domain.token.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.blog.domain.token.domain.RefreshToken;
import com.blog.domain.token.repository.RefreshTokenRepository;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;

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

	public void issueOrUpdateRefreshToken(int userId) throws Exception {
		String token = jwtUtil.createRefreshToken(String.valueOf(userId));
		LocalDateTime exp = LocalDateTime.now().plusDays(7);
		RefreshToken newRefreshToken = RefreshToken.create(userId, token, exp);

		// 이미 토큰이 존재하면 update, 없으면 insert
		if (refreshTokenRepository.existsByUserId(userId)) {
			refreshTokenRepository.update(newRefreshToken);
		} else {
			refreshTokenRepository.save(newRefreshToken);
		}
	}

	public String getRefreshTokenForUser(int userId) {
		return refreshTokenRepository.findByUserId(userId)
			.map(RefreshToken::getRefreshToken)
			.orElseThrow(() -> new CommonException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
	}
}
