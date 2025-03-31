package com.blog.domain.auth.api;

import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.response.AuthEmailResponse;
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
    public ApiResponse<AuthEmailResponse> EmailAuth(
            @RequestBody AuthEmailRequest request) throws NoSuchAlgorithmException {

        AuthEmailResponse response = authService.emailRegister(request);
        return ApiResponse.success(response);
    }

    // 카카오 회원가입
}
