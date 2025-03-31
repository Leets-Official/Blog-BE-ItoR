package com.blog.domain.login.service;

import com.blog.common.EncryptUtils;
import com.blog.common.response.ApiResponse;
import com.blog.domain.login.api.dto.request.LoginEmailRequest;
import com.blog.domain.login.api.dto.response.LoginResponse;
import com.blog.domain.users.domain.Users;
import com.blog.domain.users.service.UsersService;
import com.blog.global.security.jwt.JwtUtil;
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

    // 이메일 로그인
    public ApiResponse<LoginResponse> emailLogin(LoginEmailRequest request) throws NoSuchAlgorithmException {

        String hashedPassword = EncryptUtils.sha256(request.password());
        Users user = usersService.getUsersByEmailAndPassword(request, hashedPassword);

        if (user == null){

            return ApiResponse.error("로그인 실패");
        }

        LoginResponse response = createTokens(user, request.refreshToken());

        return ApiResponse.success(response);
    }

    // 카카오 로그인
    public Users getUsersByName(String name)  {

        return usersService.getUsersByName(name);
    }


    public LoginResponse createTokens(Users user, String refreshToken) {
        String accessToken;
        String newRefreshToken;

        if (refreshToken != null && jwtUtil.isValidRefreshToken(refreshToken)) {
            accessToken = jwtUtil.createAccessToken(user);
            newRefreshToken = refreshToken;
        } else {
            accessToken = jwtUtil.createAccessToken(user);
            newRefreshToken = jwtUtil.createRefreshToken(user);
        }

        return new LoginResponse(accessToken, newRefreshToken, user);
    }
}
