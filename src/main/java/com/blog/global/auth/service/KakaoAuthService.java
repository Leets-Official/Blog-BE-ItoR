package com.blog.global.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.auth.kakao.KakaoClient;
import com.blog.global.auth.kakao.dto.KakaoTokenResponseDto;
import com.blog.global.auth.kakao.dto.KakaoUserInfoResponseDto;

@Service
public class KakaoAuthService {

	private final KakaoClient kakaoClient;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Value("${kakao.client-id}")
	private String clientId;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@Value("${kakao.token-uri}")
	private String tokenUri;

	@Value("${kakao.user-info-uri}")
	private String userInfoUri;

	public KakaoAuthService(KakaoClient kakaoClient, UserRepository userRepository, JwtUtil jwtUtil) {
		this.kakaoClient = kakaoClient;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	public String kakaoLogin(String code) throws Exception {

		// 엑세스 토큰을 요청한다
		KakaoTokenResponseDto tokenResponse = kakaoClient.getToken(code, clientId, redirectUri, tokenUri);

		// 사용자 정보를 요청한다
		KakaoUserInfoResponseDto userInfo = kakaoClient.getUserInfo(tokenResponse.getAccessToken(), userInfoUri);

		//DB에 유저 존재 여부 확인
		String email = userInfo.getEmail();
		User user = userRepository.findByEmail(email).orElseGet(() ->
			createNewUser(userInfo));

		//jwt발급
		return jwtUtil.createToken(String.valueOf(user.getUserId()));
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
