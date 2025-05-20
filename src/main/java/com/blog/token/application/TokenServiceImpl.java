package com.blog.token.application;

import com.blog.token.domain.Token;
import com.blog.token.domain.TokenRepository;
import com.blog.token.domain.TokenService;
import com.blog.token.infrastructure.JwtTokenProvider;
import com.blog.token.presentation.dto.RefreshTokenRequest;
import com.blog.token.presentation.dto.TokenResponse;
import com.blog.user.domain.User;
import com.blog.user.domain.UserRepository;
import com.blog.user.presentation.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenServiceImpl(TokenRepository tokenRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        Long userId = Long.parseLong(jwtTokenProvider.parseUserId(refreshToken));
        Token saved = tokenRepository.findByUserId(userId);

        if (saved == null || !saved.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("토큰이 일치하지 않거나 존재하지 않습니다.");
        }

        tokenRepository.deleteByUserId(userId);

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);
        Timestamp expiresAt = jwtTokenProvider.getRefreshTokenExpiry();

        Token newToken = new Token(user.getUserId(), newRefreshToken, expiresAt, new Timestamp(System.currentTimeMillis()));
        tokenRepository.save(newToken);

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setNickname(user.getNickname());
        userResponse.setProfileImage(user.getProfileImage());

        if (user.getCreatedAt() != null) {
            userResponse.setCreatedAt(user.getCreatedAt().toString());
        }
        if (user.getUpdatedAt() != null) {
            userResponse.setUpdatedAt(user.getUpdatedAt().toString());
        }

        TokenResponse response = new TokenResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresAt(expiresAt.toString());
        response.setUser(userResponse);

        return response;
    }
}
