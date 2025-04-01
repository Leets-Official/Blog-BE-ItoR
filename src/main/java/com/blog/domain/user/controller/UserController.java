package com.blog.domain.user.controller;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.jwtUtil.JwtUtil;

import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/protected-resource")
  public User getUserInfo(@RequestHeader("Authorization") String token) {
    // "Bearer " 제거
    token = token.replace("Bearer ", "");

    // 토큰 검증 및 파싱
    Map<String, Object> claims = JwtUtil.verifyToken(token);
    String userId = (String) claims.get("id");

    // 유저 정보 조회
    Optional<User> userOpt = userRepository.findById(userId);
    return userOpt.orElseThrow(() -> new RuntimeException("User not found"));
  }
}