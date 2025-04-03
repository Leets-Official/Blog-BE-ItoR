package com.blog.domain.user.controller;

import com.blog.domain.user.service.LoginService;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }
    @PostMapping
    public String login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        return loginService.login(email, password);
    }
}