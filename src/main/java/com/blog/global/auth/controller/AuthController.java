package com.blog.global.auth.controller;

import com.blog.global.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody Map<String, String> request) {
    return authService.signup(request);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
    return authService.login(request);
  }

  @GetMapping("/kakao/callback")
  public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) {
    return authService.kakaoLogin(code);
  }
}