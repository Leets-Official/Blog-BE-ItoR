package com.blog.user.presentation;

import com.blog.user.application.UserProfileService;
import com.blog.token.infrastructure.JwtTokenProvider;
import com.blog.user.presentation.dto.UserResponse;
import com.blog.user.presentation.dto.UserUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserProfileController(UserProfileService userProfileService, JwtTokenProvider jwtTokenProvider) {
        this.userProfileService = userProfileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 유저 정보 조회
    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        return ResponseEntity.ok(userProfileService.getUser(userId));
    }

    // 유저 정보 수정
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(
            @RequestHeader("Authorization") String token,
            @RequestBody UserUpdateRequest request
    ) {
        Long userId = extractUserId(token);
        return ResponseEntity.ok(userProfileService.updateUser(userId, request));
    }

    // 유저 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        userProfileService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    private Long extractUserId(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Long.parseLong(jwtTokenProvider.parseUserId(token));
    }
}
