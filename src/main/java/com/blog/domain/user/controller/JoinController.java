package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.service.UserService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/join")
public class JoinController {

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 - 프론트에서 email, name , provider를 넣어서 보내준 상태
    @PostMapping
    public ApiResponse<String> join(@Valid @RequestBody JoinRequest joinRequest) throws NoSuchAlgorithmException {

        // email+nickname도 중복 안됨
        boolean isEmailUsed = userService.isEmailUsed(joinRequest.getEmail());
        if (isEmailUsed) {
            return ApiResponse.fail(new CustomException(ErrorCode.DUPLICATE_EMAIL));
        }

        userService.join(joinRequest);
        return ApiResponse.ok("회원가입이 완료되었습니다.");
    }
}
