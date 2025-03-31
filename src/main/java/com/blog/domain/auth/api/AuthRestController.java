package com.blog.domain.auth.api;

import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.request.AuthKaKaoRequest;
import com.blog.domain.auth.api.dto.response.AuthResponse;
import com.blog.domain.auth.api.dto.response.AuthKakaoResponse;
import com.blog.domain.auth.service.AuthService;
import com.blog.common.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService){
        this.authService = authService;
    }


    // 이메일 회원가입 (이메일, 닉네임 중복확인)
    @PostMapping("/signup/email")
    public ApiResponse<AuthResponse> EmailAuth(
            @RequestBody AuthEmailRequest request) throws NoSuchAlgorithmException {

        AuthResponse response = authService.emailRegister(request);

        return ApiResponse.success(response);
    }

    // 카카오 회원가입 토큰 받기
    @GetMapping("/kakao/register")
    public ApiResponse<AuthKakaoResponse> KakaoAuth(
            @RequestParam("code") String code) {

        return authService.getAccessTokenKakao(code);
    }

    // 카카오 추가 회원가입
    @PostMapping("/kakao/register/info")
    public ApiResponse<AuthResponse> KakaoAuthInfo(
            @RequestBody AuthKaKaoRequest request) {

        AuthResponse response = authService.kakaoRegister(request);

        return ApiResponse.success(response);
    }

}
