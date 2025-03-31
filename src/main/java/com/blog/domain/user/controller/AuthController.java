package com.blog.domain.user.controller;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.KakaoService;
import com.blog.domain.user.service.LoginService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.CustomTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.blog.global.exception.ErrorCode.INVALID_ACCESS_TOKEN;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService userService;
    private final KakaoService kakaoService;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public AuthController(LoginService userService, KakaoService kakaoService) {
        this.userService = userService;
        this.kakaoService = kakaoService;
    }

    // 회원가입 - 프론트에서 email, name , provider를 넣어서 보내준 상태
    @PostMapping
    public ApiResponse<String> join(@Valid @RequestBody JoinRequest joinRequest) {

        // email+nickname도 중복 안됨
        boolean isEmailUsed = userService.isEmailUsed(joinRequest.getEmail());
        if (isEmailUsed) {
            return ApiResponse.fail(new CustomException(ErrorCode.DUPLICATE_EMAIL));
        }
        userService.join(joinRequest);
        return ApiResponse.ok("회원가입이 완료되었습니다.");
    }


    // 카카오 로그인 성공후 - 프론트에서 code전달 후 accesstoken 요청
    // 완료 후 프론트에서 accessToken을 가지고 있는 상태
    @GetMapping("kakao/callback")
    public ApiResponse<String> kakaoCallback(@RequestParam("code") String authorizationCode) {
        String accessToken = kakaoService.getAccessToken(authorizationCode, clientId, redirectUri);
        return ApiResponse.ok(accessToken);
    }

    // 프론트에서 accessToken을 넘겨줘서 유저가 db에 있는지 확인
    // 있으면 main으로 없으면 회원가입창으로 넘어가기
    @PostMapping("kakao/loginRequest")
    public ApiResponse<String> loginRequest(@RequestParam(value = "access_token") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }

        boolean isAccessTokenValid=kakaoService.isAccessTokenValid(accessToken);
        Map<String, Object> userInfo = kakaoService.getKakaoUserInfo(accessToken);
        // 유저가 DB에 있는지 확인
        Optional<User> existingUser = userService.findByKakaoInfo(userInfo);
        System.out.println("controller" + existingUser);

        if (existingUser.isPresent()) {
            String generateAccessToken = CustomTokenUtil.generateAccessToken(existingUser.get().getId(),existingUser.get().getEmail()); // 기존 유저의 AccessToken 생성
           
            // 기존 회원 -> 로그인 로직후 메인 페이지 이동
            return ApiResponse.ok("로그인 성공! 메인 페이지로 이동: " + generateAccessToken);
            //return "redirect:http://localhost:3000/main?access_token=" + accessToken;

        } else {
            // 회원이 아니면 회원가입 여부를 묻는 모달 띄우기
            String generateAccessToken = CustomTokenUtil.generateAccessToken(existingUser.get().getId(),existingUser.get().getEmail()); // 기존 유저의 AccessToken 생성

            return ApiResponse.ok("회원이 존재하지 않음. 회원가입 여부 확인 모달로 이동: " + generateAccessToken);
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

    // 액세스 토큰 갱신 요청
    @PostMapping("kakao/refresh-token")
    public ApiResponse<String> refreshToken(@RequestParam(value = "refresh_token") String refreshToken) {
        // 리프레시 토큰을 사용하여 액세스 토큰 갱신
        String newAccessToken = kakaoService.refreshAccessToken(refreshToken);
        return ApiResponse.ok(newAccessToken);
    }

}
