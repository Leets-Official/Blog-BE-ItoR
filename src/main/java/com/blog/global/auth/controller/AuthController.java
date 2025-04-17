package com.blog.global.auth.controller;

import com.blog.domain.user.dto.SignupRequest;
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
  public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequest request) {
    return authService.signup(request);
  }

  @GetMapping("/kakao/callback")
  public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) {
    System.out.println("✅ Kakao 로그인 진입: code = " + code);
    return authService.kakaoLogin(code);
  }
}
