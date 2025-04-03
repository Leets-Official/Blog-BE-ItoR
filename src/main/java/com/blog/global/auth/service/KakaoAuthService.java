package com.blog.global.auth.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blog.domain.token.domain.RefreshToken;
import com.blog.domain.token.repository.RefreshTokenRepository;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.dto.LoginResponseDto;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.auth.kakao.KakaoClient;
import com.blog.global.auth.kakao.dto.KakaoTokenResponseDto;
import com.blog.global.auth.kakao.dto.KakaoUserInfoResponseDto;

@Service
public class KakaoAuthService {

	private final KakaoClient kakaoClient;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${kakao.client-id}")
	private String clientId;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@Value("${kakao.token-uri}")
	private String tokenUri;

	@Value("${kakao.user-info-uri}")
	private String userInfoUri;

	public KakaoAuthService(KakaoClient kakaoClient, UserRepository userRepository, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
		this.kakaoClient = kakaoClient;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public LoginResponseDto kakaoLogin(String code) throws Exception {

		// 엑세스 토큰을 요청한다
		KakaoTokenResponseDto tokenResponse = kakaoClient.getToken(code, clientId, redirectUri, tokenUri);

		// 사용자 정보를 요청한다
		KakaoUserInfoResponseDto userInfo = kakaoClient.getUserInfo(tokenResponse.getAccessToken(), userInfoUri);

		//DB에 유저 존재 여부 확인
		String email = userInfo.getEmail();
		User user = userRepository.findByEmail(email).orElseGet(() ->
			createNewUser(userInfo));

		String accessToken = jwtUtil.createToken(String.valueOf(user.getUserId()));
		String refreshToken = jwtUtil.createRefreshToken(String.valueOf(user.getUserId()));

		RefreshToken token = RefreshToken.create(user.getUserId(), refreshToken, LocalDateTime.now().plusDays(7));
		refreshTokenRepository.save(token);

		return new LoginResponseDto(accessToken, refreshToken);
	}

	private User createNewUser(KakaoUserInfoResponseDto userInfo) {
		User newUser = User.createKakaoUser(
			userInfo.getNickname(),
			userInfo.getEmail()
		);
		userRepository.save(newUser);
		return newUser;
	}
}
