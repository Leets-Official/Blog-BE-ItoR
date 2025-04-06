package com.blog.domain.user.controller;

import com.blog.domain.user.domain.User;
import com.blog.global.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private AuthService authService = new AuthService(null);

  @GetMapping("/protected-resource")
  public ResponseEntity<User> getProtectedResource(
    @RequestHeader("Authorization") String token) {
    User user = authService.getUserInfoFromToken(token);
    return ResponseEntity.ok(user);
  }
}
