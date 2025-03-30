package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.JoinRequest;
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

    // 이메일 로그인
//    @PostMapping("/login")
//    public ApiResponse<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
//        Optional<User> userOpt = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            request.getSession().setAttribute("user", user);
//            return ApiResponse.ok("로그인 성공");
//        }
//        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
//    }

//    @PostMapping("/logout")
//    public ApiResponse<String> logout(HttpServletRequest request) {
//        request.getSession().invalidate();
//        return ApiResponse.ok("로그아웃 성공");
//    }

}