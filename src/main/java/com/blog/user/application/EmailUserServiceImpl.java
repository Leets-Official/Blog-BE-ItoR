package com.blog.user.application;

import com.blog.token.presentation.dto.TokenResponse;
import com.blog.user.presentation.dto.EmailLoginRequest;
import com.blog.user.presentation.dto.UserSignupRequest;
import com.blog.user.presentation.dto.UserResponse;
import com.blog.token.domain.Token;
import com.blog.token.domain.TokenRepository;
import com.blog.token.infrastructure.JwtTokenProvider;
import com.blog.user.domain.User;
import com.blog.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class EmailUserServiceImpl implements UserService, LoginService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public EmailUserServiceImpl(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public TokenResponse login(Object request) {
        EmailLoginRequest emailRequest = (EmailLoginRequest) request;

        User user = userRepository.findByEmail(emailRequest.getEmail());
        if (user == null || !user.getPassword().equals(emailRequest.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        Timestamp expiresAt = jwtTokenProvider.getRefreshTokenExpiry();

        Token token = new Token(
                user.getUserId(),
                refreshToken,
                expiresAt,
                new Timestamp(System.currentTimeMillis())
        );
        tokenRepository.save(token);

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

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        tokenResponse.setExpiresAt(expiresAt.toString());
        tokenResponse.setUser(userResponse);

        return tokenResponse;
    }

    @Override
    public UserResponse signup(UserSignupRequest request) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        User user = new User(
                null,
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getNickname(),
                request.getBirthDate(),
                request.getProfileImage(),
                request.getIntroduction(),
                request.getAuthMethod(),
                now,
                now,
                true
        );

        userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setNickname(user.getNickname());
        response.setProfileImage(user.getProfileImage());
        response.setCreatedAt(user.getCreatedAt().toString());

        return response;
    }

    @Override
    public String getLoginType() {
        return "email";
    }
}
