package com.blog.domain.user.service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.security.PasswordUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private final UserRepository userRepository;

    // 의존성
    private final TokenService tokenService;

    public LoginService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    // 이메일로 user확인, 현재 비밀번호 확인
    public String authenticateUser(String email, String password, HttpServletResponse response) {

        // 유저가 존재하는지 예외체크
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // AccessToken 발급
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getEmail());

        tokenService.generateRefreshToken(user.getId(), user.getEmail(), response);
        return accessToken;
    }


}
