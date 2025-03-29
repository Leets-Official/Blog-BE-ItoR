package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.application.in.user.LoginUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final LoginUseCase loginService;

    public LogoutController(LoginUseCase loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ApiResponse<String> logout(
            HttpServletResponse response, HttpServletRequest request) {
        loginService.logout(response, request);

        return ApiResponse.ok("로그 아웃 되었습니다.");
    }

}
