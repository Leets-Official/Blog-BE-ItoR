package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.LoginRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.TokenStore;
import com.blog.domain.user.service.LoginService;
import com.blog.domain.user.service.TokenService;
import com.blog.global.exception.CustomException;
import com.blog.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static com.blog.global.exception.ErrorCode.*;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private final TokenService tokenService;

    public LoginController(LoginService loginService, TokenService tokenService, TokenStore tokenStore) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }


    // 이메일 로그인
    @PostMapping
    public ApiResponse<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        // 사용자 정보 검증
        String accessToken = loginService.authenticateUser(loginRequest.email(), loginRequest.password(), response);

        return ApiResponse.ok(accessToken);
    }

    // refresh 토큰으로 accessToken 재발급
    @PostMapping("/refresh")
    public ApiResponse<String> refresh(HttpServletRequest request) {

        String newAccessToken = tokenService.getAccessTokenFromRequest(request);

        return ApiResponse.ok(newAccessToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {

        tokenService.deleteRefreshTokenByLogout(request);

        return ApiResponse.ok("로그아웃 성공");
    }



}
