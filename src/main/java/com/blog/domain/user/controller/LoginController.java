package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.LoginRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.TokenStore;
import com.blog.domain.user.service.LoginService;
import com.blog.domain.user.service.TokenService;
import com.blog.global.exception.CustomException;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.CustomTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

import static com.blog.global.exception.ErrorCode.*;


/*
 *    회원가입, 로그인, 로그아웃, 회원 탈퇴,
 * */

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
        System.out.println("로그인 요청: " + loginRequest.getEmail() + ", " + loginRequest.getPassword());

        User user = loginService.authenticateUser(loginRequest.getEmail(),loginRequest.getPassword());
        System.out.println("controller"+user);

        if (user != null) {
            // JWT 토큰 생성
            tokenService.generateRefreshToken(user.getId(), user.getEmail());
            String accessToken =tokenService.generateAccessToken(user.getId(), user.getEmail());
            System.out.println("email 로그인"+accessToken);
            tokenService.addCookie(user.getId().toString(), response);
            return ApiResponse.ok("accessToken: "+ accessToken);  // 로그인 성공 시 토큰 반환
        } else {
            return ApiResponse.fail(new CustomException(INVALID_TOKEN));  // 로그인 성공 시 토큰 반환
        }}

    // refresh토큰으로 accessToken 재발급
    @PostMapping("/refresh")
    public ApiResponse<String> refresh(HttpServletRequest request) {

        // 쿠키에서 사용자 ID 가져오기
        Long userId = tokenService.getUserIdFromCookie(request);
        if (userId == null) {
            throw new CustomException(USER_NOT_FOUND_IN_COOKIE);
        }

        // 인메모리 저장소에서 refreshToken 가져오기
        String refreshToken = String.valueOf(tokenStore.getToken(userId));
        System.out.println("refresh재발급Token" + refreshToken);

        // refreshToken으로 accessToken 재발급
        Map<String, Object> user = tokenService.decodeUserFromToken(refreshToken);

        // "userId"가 String으로 저장되어 있을 때 Long으로 변환
        Long userIdfromAccessToken = Long.parseLong((String) user.get("userId"));

    // "email"은 이미 String으로 저장되어 있으므로 그대로 사용
        String userEmailfromAccessToken = (String) user.get("email");

        String newAccessToken = tokenService.generateAccessToken(userIdfromAccessToken, userEmailfromAccessToken);
        return ApiResponse.ok(newAccessToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);

        // 토큰에서 사용자 정보 추출 (디코딩)
        Map<String, Object> user = tokenService.decodeUserFromToken(token);

        // 사용자 ID 추출 (userId는 디코딩된 사용자 정보에서 가져옵니다)
        Long userId = Long.valueOf(user.get("userId").toString()); // userId가 Long 형식이라면

        // 인메모리에서 토큰 삭제
        tokenStore.removeToken(userId);

        return ApiResponse.ok("로그아웃 성공");
    }



}