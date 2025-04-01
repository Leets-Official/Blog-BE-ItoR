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
    private final TokenStore tokenStore;

    public LoginController(LoginService loginService, TokenService tokenService, TokenStore tokenStore) {
        this.loginService = loginService;
        this.tokenService = tokenService;
        this.tokenStore = tokenStore;
    }


    // 이메일 로그인
    @PostMapping
    public ApiResponse<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        // 사용자 정보 검증
        User user = loginService.authenticateUser(loginRequest.getEmail(),loginRequest.getPassword());

        if (user != null) {
            tokenService.generateRefreshToken(user.getId(), user.getEmail());
            String accessToken =tokenService.generateAccessToken(user.getId(), user.getEmail());
            tokenService.addCookie(user.getId().toString(), response);
            return ApiResponse.ok(accessToken);
        } else {
            return ApiResponse.fail(new CustomException(INVALID_TOKEN));
        }}

    // refresh 토큰으로 accessToken 재발급
    @PostMapping("/refresh")
    public ApiResponse<String> refresh(HttpServletRequest request) {

        // 쿠키에서 사용자 ID 가져오기
        Long userId = tokenService.getUserIdFromCookie(request);
        if (userId == null) {
            throw new CustomException(USER_NOT_FOUND_IN_COOKIE);
        }
        String refreshToken = String.valueOf(tokenStore.getToken(userId));
        Map<String, Object> user = tokenService.decodeUserFromToken(refreshToken);

        Long userIdfromAccessToken = Long.parseLong((String) user.get("userId"));
        String userEmailfromAccessToken = (String) user.get("email");

        String newAccessToken = tokenService.generateAccessToken(userIdfromAccessToken, userEmailfromAccessToken);
        return ApiResponse.ok(newAccessToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        Map<String, Object> user = tokenService.decodeUserFromToken(token);  // 토큰에서 사용자 정보 추출 (디코딩)

        Long userId = Long.valueOf(user.get("userId").toString()); // userId가 Long 형식이라면
        tokenStore.removeToken(userId);

        return ApiResponse.ok("로그아웃 성공");
    }



}