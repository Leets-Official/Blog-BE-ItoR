package com.blog.domain.auth.api;

import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.request.AuthKaKaoRequest;
import com.blog.domain.auth.api.dto.response.AuthResponse;
import com.blog.domain.auth.api.dto.response.AuthKakaoResponse;
import com.blog.domain.auth.service.AuthService;
import com.blog.common.response.ApiResponse;
import com.blog.domain.login.api.dto.response.LoginResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(
            AuthService authService){

        this.authService = authService;
    }


    // 이메일 회원가입 (이메일, 닉네임 중복확인)
    @PostMapping("/email/register")
    public ApiResponse<AuthResponse> emailAuth(
            @RequestBody AuthEmailRequest request) {

        AuthResponse response = authService.addUserByEmail(request);

        return ApiResponse.ok(response);
    }

    // 카카오 회원가입 토큰 받고 회원 있는지 확인
    @GetMapping("/kakao/register")
    public ApiResponse<LoginResponse> kakaoAuth(
            @RequestParam("code") String code) {

        AuthKakaoResponse kakaoToken = authService.getAccessTokenKakao(code);
        return authService.getUsersByKakaoToken(kakaoToken);
    }


    // 회원이 아니라면 추가 정보 받아서 회원가입
    @PostMapping("/kakao/register/info")
    public ApiResponse<AuthResponse> kakaoAuthInfo(
            @RequestBody AuthKaKaoRequest request) {

        AuthResponse response = authService.addUserByKakao(request);

        return ApiResponse.ok(response);
    }

}
