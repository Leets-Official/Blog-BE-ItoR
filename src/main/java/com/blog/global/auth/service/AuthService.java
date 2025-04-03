package com.blog.global.auth.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.blog.domain.token.domain.RefreshToken;
import com.blog.domain.token.repository.RefreshTokenRepository;
import com.blog.domain.token.service.TokenService;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.dto.LoginResponseDto;
import com.blog.global.auth.dto.SignUpRequestDto;
import com.blog.global.auth.dto.SignUpResponseDto;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;
	private final TokenService tokenService;

	public AuthService(UserRepository userRepository, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.refreshTokenRepository = refreshTokenRepository;
		this.tokenService = tokenService;
	}

	public LoginResponseDto login(String email, String password) throws Exception {

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

		//비밀번호 일치 검사 (평문 비교, 추후 해시로 개선 가능)
		if (!user.getPassword().equals(password)) {
			throw new CommonException(ErrorCode.INVALID_PASSWORD);
		}

		String accessToken = jwtUtil.createToken(String.valueOf(user.getUserId()));
		tokenService.issueOrUpdateRefreshToken(user.getUserId());
		String refreshToken = tokenService.getRefreshTokenForUser(user.getUserId());


		return new LoginResponseDto(accessToken, refreshToken);
	}

	public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

		if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
			throw new CommonException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		if (!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordConfirm())) {
			throw new CommonException(ErrorCode.PASSWORD_MISMATCH);
		}

		User user = User.createEmailUser(
			signUpRequestDto.getName(),
			signUpRequestDto.getNickName(),
			signUpRequestDto.getEmail(),
			signUpRequestDto.getPassword(),
			signUpRequestDto.getBirthDate(),
			signUpRequestDto.getIntroduce(),
			signUpRequestDto.getProfileImageUrl()
		);

		userRepository.save(user);

		return new SignUpResponseDto("회원가입 성공!");
	}

	// 어세스 토큰 재발급 로직
	public LoginResponseDto reissueAccessToken(String refreshToken) throws Exception {
		if (!jwtUtil.validateToken(refreshToken)) {
			throw new CommonException(ErrorCode.INVALID_TOKEN);
		}

		String userIdStr = jwtUtil.extractUserId(refreshToken);
		int userId = Integer.parseInt(userIdStr);

		RefreshToken storedToken = refreshTokenRepository.findByUserId(userId)
			.orElseThrow(() -> new CommonException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

		if (!storedToken.getRefreshToken().equals(refreshToken)) {
			throw new CommonException(ErrorCode.INVALID_TOKEN);
		}

		String newAccessToken = jwtUtil.createToken(String.valueOf(userId));
		String newRefreshToken = jwtUtil.createRefreshToken(String.valueOf(userId));
		LocalDateTime newExp = LocalDateTime.now().plusDays(7);

		storedToken.update(newRefreshToken, newExp);
		refreshTokenRepository.update(storedToken);

		return new LoginResponseDto(newAccessToken, newRefreshToken);
	}

}

