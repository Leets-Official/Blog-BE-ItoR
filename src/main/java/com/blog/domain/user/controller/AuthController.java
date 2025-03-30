package com.blog.domain.user.controller;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.KakaoService;
import com.blog.domain.user.service.UserService;
import com.blog.global.exception.CustomException;
import com.blog.global.response.ApiResponse;
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

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public AuthController(UserService userService, KakaoService kakaoService) {
        this.userService = userService;
        this.kakaoService = kakaoService;
    }

    // 로그인 성공후 프론트에서 code전달 후 accesstoken 요청
    // 완료 후 프론트에서 accessToken을 가지고 있는 상태
    @GetMapping("kakao/callback")
    public ApiResponse<String> kakaoCallback(@RequestParam("code") String authorizationCode) {
        String accessToken = kakaoService.getAccessToken(authorizationCode, clientId, redirectUri);
        return ApiResponse.ok(accessToken);
    }


    // 로그인 요청하기
    @PostMapping("kakao/loginRequest")
    public ApiResponse<String> loginRequest(@RequestParam(value = "access_token") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }


        Map<String, Object> userInfo = kakaoService.getKakaoUserInfo(accessToken);
        // 유저가 DB에 있는지 확인
        Optional<User> existingUser = userService.findByKakaoInfo(userInfo);
        System.out.println("controller"+existingUser);

        if (existingUser.isPresent()) {
            // 기존 회원 -> 로그인 로직후 메인 페이지 이동
//            userService.loginFilterlogic(userInfo);
            return ApiResponse.ok("로그인 성공! 메인 페이지로 이동: " + accessToken);
            //return "redirect:http://localhost:3000/main?access_token=" + accessToken;

        } else {
            // 회원이 아니면 회원가입 여부를 묻는 모달 띄우기
            return ApiResponse.ok("회원이 존재하지 않음. 회원가입 여부 확인 모달로 이동: " + accessToken);
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
