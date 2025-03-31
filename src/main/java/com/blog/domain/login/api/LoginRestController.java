package com.blog.domain.login.api;

import com.blog.common.response.ApiResponse;
import com.blog.domain.login.api.dto.request.LoginEmailRequest;
import com.blog.domain.login.api.dto.response.LoginResponse;
import com.blog.domain.login.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/login")
public class LoginRestController {

    private final LoginService loginService;

    public LoginRestController(LoginService loginService){

        this.loginService = loginService;
    }

    // 이메일 로그인
    @PostMapping("/email")
    public ApiResponse<LoginResponse> emailLogin(
            @RequestBody LoginEmailRequest request) throws NoSuchAlgorithmException {

        return loginService.emailLogin(request);
    }
}
