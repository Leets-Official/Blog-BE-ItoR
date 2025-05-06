package com.blog.domain.user.controller;

import com.blog.domain.user.controller.request.UpdateRequest;
import com.blog.domain.user.controller.response.MypageResponse;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.MyPageService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
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
    public ApiResponse<MypageResponse> getMyPage(@GetUserId Long userId) {
        User user = myPageService.getMyPageInfo(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        MypageResponse myPageInfo = MypageResponse.from(user);

        return ApiResponse.ok(myPageInfo);
    }

    // 정보 수정
    @PutMapping
    public ApiResponse<String> updateMyPage(@GetUserId Long userId,
                                          @RequestBody @Valid UpdateRequest userRequest) {

        myPageService.updateMyPageInfo(userId, userRequest);
        return ApiResponse.ok("정상적으로 수정되었습니다.");
    }


}
