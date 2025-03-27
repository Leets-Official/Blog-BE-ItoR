package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.request.UserLoginRequest;
import com.blog.workspace.application.in.user.LoginUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginUseCase loginService;

    public LoginController(LoginUseCase loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ApiResponse<String> login(@RequestBody UserLoginRequest request) {

        boolean login = loginService.login(request);

        return ApiResponse.ok("로그인 되었습니다.");
    }
}
