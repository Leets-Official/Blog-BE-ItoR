package com.blog.domain.login.service;

import com.blog.common.EncryptUtils;
import com.blog.common.response.ApiResponse;
import com.blog.domain.login.api.dto.request.LoginRequest;
import com.blog.domain.login.api.dto.response.LoginResponse;
import com.blog.domain.users.domain.Users;
import com.blog.domain.users.service.UsersService;
import com.blog.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Jwt;
import org.apache.catalina.User;
import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class LoginService {

    private final JwtUtil jwtUtil;
    private final UsersService usersService;

    public LoginService(JwtUtil jwtUtil, UsersService usersService){
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    public ApiResponse<LoginResponse> emailLogin(LoginRequest request) throws NoSuchAlgorithmException {

        String hashedPassword = EncryptUtils.sha256(request.password());
        Users user = usersService.emailLogin(request, hashedPassword);

        if (user == null){
            return ApiResponse.error("로그인 실패");
        }

        String accessToken;
        String refreshToken;

        if (jwtUtil.isValidRefreshToken(request.refreshToken())){
            accessToken = jwtUtil.createAccessToken(user);
            refreshToken = request.refreshToken();
        } else {
            accessToken = jwtUtil.createAccessToken(user);
            refreshToken = jwtUtil.createRefreshToken(user);
        }

        return ApiResponse.success(new LoginResponse(accessToken, refreshToken, user));
    }
}
