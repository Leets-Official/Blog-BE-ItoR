package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.controller.dto.request.LoginRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.UserService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
*    회원가입, 로그인, 로그아웃, 회원 탈퇴,
* */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 프론트에서 email, name , provider를 넣어서 보내준 상태
    @PostMapping("/join")
    public ApiResponse<String> join(@Valid @RequestBody JoinRequest joinRequest) {

        boolean isEmailUsed = userService.isEmailUsed(joinRequest.getEmail());
        if (isEmailUsed) {
            return ApiResponse.fail(new CustomException(ErrorCode.DUPLICATE_EMAIL));
        }
        userService.join(joinRequest);
        return ApiResponse.ok("회원가입이 완료되었습니다.");
    }



}