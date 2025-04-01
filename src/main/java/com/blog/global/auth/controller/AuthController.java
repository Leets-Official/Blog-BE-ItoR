package com.blog.global.auth.controller;

import com.blog.global.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public String login(@RequestBody Map<String, String> request) {
    return authService.login(request.get("email"), request.get("password"));
  }

  @GetMapping("/kakao/callback")
  public String kakaoLogin(@RequestParam("code") String code) {
    return authService.kakaoLogin(code);
  }
}