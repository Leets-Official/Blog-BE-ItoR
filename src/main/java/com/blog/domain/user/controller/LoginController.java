package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.LoginRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.TokenStore;
import com.blog.domain.user.service.LoginService;
import com.blog.global.exception.CustomException;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.CustomTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenStore tokenStore;

    public LoginController(LoginService loginService, TokenStore tokenStore) {
        this.loginService = loginService;
        this.tokenStore = tokenStore;
    }

    // 이메일 로그인
    @PostMapping
    public ApiResponse<String> login(@RequestBody LoginRequest loginRequest) {
        // 사용자 정보 검증
        User user = loginService.authenticateUser(loginRequest.getEmail(),loginRequest.getPassword());

        if (user != null) {
            // JWT 토큰 생성
            String accessToken  = CustomTokenUtil.generateAccessToken(user.getId(), user.getEmail());
            String refreshToken  = CustomTokenUtil.generateRefreshToken(user.getId());
            tokenStore.storeToken(user.getId(),refreshToken);
            System.out.println("refresh: "+refreshToken);
            return ApiResponse.ok("accessToken: "+ accessToken);  // 로그인 성공 시 토큰 반환
        } else {
            return ApiResponse.fail(new CustomException(INVALID_TOKEN));  // 로그인 성공 시 토큰 반환
        }}

    // refresh토큰으로 accessToken 재발급
    @PostMapping("/refresh")
    public ApiResponse<String> refresh(@RequestParam("refreshToken") String refreshToken) {
        if (!CustomTokenUtil.validateToken(refreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }

        Map<String, Object> user = CustomTokenUtil.getUserFromToken(refreshToken);
        System.out.println("refreshToken"+user);
        String newAccessToken = CustomTokenUtil.generateAccessToken(Long.parseLong(user.get("id").toString()), (String)user.get("email"));;
        return ApiResponse.ok(newAccessToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = Long.valueOf(getUserIdFromToken(token));  // 토큰에서 사용자 ID 추출

        // 인메모리에서 토큰 삭제
        tokenStore.removeToken(userId);

        return ApiResponse.ok("로그아웃 성공");
    }
    // 토큰을 요청에서 추출하는 방법
    private String getTokenFromRequest(HttpServletRequest request) {
        // 예시로 Authorization 헤더에서 'Bearer <token>' 형식으로 토큰을 가져온다고 가정
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // "Bearer " 부분 제거 후 토큰 반환
        }
        return null;
    }

    // 토큰에서 사용자 ID를 추출하는 로직 (예: JWT를 파싱하여 사용자 ID를 추출)
    private String getUserIdFromToken(String token) {
        // JWT 토큰을 파싱하여 사용자 ID를 추출하는 로직 구현
        // 여기서는 단순히 토큰을 문자열로 처리하는 예시입니다.
        return token.split("\\.")[0];  // 토큰의 첫 번째 부분을 사용자 ID로 가정
    }
}