package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.UpdateRequest;
import com.blog.domain.user.domain.Provider;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.MyPageService;
import com.blog.domain.user.service.UserService;
import com.blog.global.exception.CustomException;
import com.blog.global.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.blog.global.exception.ErrorCode.INVALID_LOGIN;
import static com.blog.global.exception.ErrorCode.USER_NOT_FOUND;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final UserService userService;


    public MyPageController(MyPageService myPageService, UserService userService) {
        this.myPageService = myPageService;
        this.userService = userService;
    }

    // 정보 조회
    @GetMapping
    public ApiResponse<User> getMyPage(@CookieValue(value = "userId", required = false) Long userId) {
        if (userId == null) {
            throw new CustomException(INVALID_LOGIN);
        }

        User myPageInfo = myPageService.getMyPageInfo(userId);
        return ApiResponse.ok(myPageInfo);
    }

    // 정보 수정
    @PutMapping
    public ApiResponse<User> updateMyPage(@CookieValue(value = "userId", required = false) Long userId,
                                          @RequestBody @Valid UpdateRequest userRequest) {
        if (userId == null) {
            throw new CustomException(INVALID_LOGIN);
        }

        User updatedUser = myPageService.updateMyPageInfo(userId, userRequest); //
        return ApiResponse.ok(updatedUser);
    }




}
