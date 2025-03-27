package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.request.UserRegisterRequest;
import com.blog.workspace.application.in.user.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerService;

    /// 생성자
    public AuthController(RegisterUserUseCase registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public ApiResponse<String> register(@RequestBody @Valid UserRegisterRequest request) {

        registerService.registerUser(request);
        return ApiResponse.ok("회원 가입 성공했습니다.");
    }


}
