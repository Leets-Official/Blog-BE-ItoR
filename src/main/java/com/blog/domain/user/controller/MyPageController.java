package com.blog.domain.user.controller;

import com.blog.domain.user.controller.request.UpdateRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.MyPageService;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.aop.GetUserId;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    // 정보 조회
    @GetMapping
    public ApiResponse<User> getMyPage(@GetUserId Long userId) {
        User myPageInfo = myPageService.getMyPageInfo(userId);
        return ApiResponse.ok(myPageInfo);
    }

    // 정보 수정
    @PutMapping
    public ApiResponse<User> updateMyPage(@GetUserId Long userId,
                                          @RequestBody @Valid UpdateRequest userRequest) {

        User updatedUser = myPageService.updateMyPageInfo(userId, userRequest); //
        return ApiResponse.ok(updatedUser);
    }


}
