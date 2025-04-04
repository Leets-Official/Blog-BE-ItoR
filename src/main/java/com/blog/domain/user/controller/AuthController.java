package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.KakaoService;
import com.blog.domain.user.service.LoginService;
import com.blog.domain.user.service.TokenService;
import com.blog.domain.user.service.UserService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.CustomTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.blog.global.exception.ErrorCode.INVALID_ACCESS_TOKEN;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final KakaoService kakaoService;

    public AuthController(UserService userService, KakaoService kakaoService) {
        this.userService = userService;
        this.kakaoService = kakaoService;
    }

    @GetMapping("kakao/callback")
    public ApiResponse<String> kakaoCallback(@RequestParam("code") String authorizationCode) {
        String accessToken = kakaoService.getAccessToken(authorizationCode);
        return ApiResponse.ok(accessToken);
    }

    @PostMapping("kakao/loginRequest")
    public ApiResponse<String> loginRequest(@RequestParam(value = "access_token") String accessToken, HttpServletResponse response) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }

        Map<String, Object> userInfo = kakaoService.getKakaoUserInfo(accessToken);
        Optional<User> existingUser = userService.findByKakaoInfo(userInfo);

        if (existingUser.isPresent()) {
            return ApiResponse.ok("로그인 성공! 메인 페이지로 이동: ");
            //return "redirect:http://localhost:3000/main?access_token=" + accessToken;

        } else {
            return ApiResponse.ok("회원이 존재하지 않음. 회원가입 여부 확인 모달로 이동: ");
            //return "redirect:http://localhost:3000/loginRequestModal?access_token=" + accessToken;

        }
    }

    // 프론트에서 accessToken으로 회원가입 요청하면 유저 정보를 줌
    @GetMapping("kakao/signupRequest")
    public ApiResponse<Map<String, Object>> signupRequest(@RequestParam(value = "access_token") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }
        Map<String, Object> userInfo = kakaoService.getKakaoUserInfo(accessToken);
        return ApiResponse.ok(userInfo);
    }

}
