package com.blog.domain.user.controller;

import com.blog.domain.user.controller.request.JoinRequest;
import com.blog.domain.user.service.UserService;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.aop.GetUserId;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/join")
public class JoinController {

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<String> join(@Valid @RequestBody JoinRequest joinRequest) throws NoSuchAlgorithmException {
        userService.join(joinRequest);
        return ApiResponse.ok("회원가입이 완료되었습니다.");
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@GetUserId Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.ok("회원탈퇴가 완료되었습니다.");

    }

}
