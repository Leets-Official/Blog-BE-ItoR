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
    private final TokenService tokenService;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public AuthController(UserService userService, KakaoService kakaoService, TokenService tokenService) {
        this.userService = userService;
        this.kakaoService = kakaoService;
        this.tokenService = tokenService;
    }



    // 카카오 로그인 성공후 - 프론트에서 code전달 후 accesstoken 요청 0
    // 완료 후 프론트에서 accessToken을 가지고 있는 상태
    @GetMapping("kakao/callback")
    public ApiResponse<String> kakaoCallback(@RequestParam("code") String authorizationCode) {
        System.out.println("Received Authorization Code: " + authorizationCode); // 디버깅 로그 추가
        String accessToken = kakaoService.getAccessToken(authorizationCode, clientId, redirectUri);
        return ApiResponse.ok(accessToken + " access_token");
    }

    // 프론트에서 accessToken을 넘겨줘서 유저가 db에 있는지 확인
    // 있으면 main으로 없으면 회원가입창으로 넘어가기
    // 원래는 쿠키에 저장된 토큰을 통해 자동으로 인증
    @PostMapping("kakao/loginRequest")
    public ApiResponse<String> loginRequest(@RequestParam(value = "access_token") String accessToken, HttpServletResponse response) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }

        Map<String, Object> userInfo = kakaoService.getKakaoUserInfo(accessToken,response);
        // 유저가 DB에 있는지 확인
        Optional<User> existingUser = userService.findByKakaoInfo(userInfo);
        System.out.println("controller" + existingUser);

        if (existingUser.isPresent()) {
            // 기존 회원 -> 로그인 로직후 메인 페이지 이동
            return ApiResponse.ok("로그인 성공! 메인 페이지로 이동: ");
            //return "redirect:http://localhost:3000/main?access_token=" + accessToken;

        } else {
            // 회원이 아니면 회원가입 여부를 묻는 모달 띄우기
            return ApiResponse.ok("회원이 존재하지 않음. 회원가입 여부 확인 모달로 이동: ");
            //return "redirect:http://localhost:3000/loginRequestModal?access_token=" + accessToken;

        }
    }

    // 프론트에서 accessToken으로 회원가입 요청하면 유저 정보를 줌 0
    @GetMapping("kakao/signupRequest")
    public ApiResponse<Map<String, Object>> signupRequest(@RequestParam(value = "access_token") String accessToken,HttpServletResponse response) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }
        Map<String, Object> userInfo = kakaoService.getKakaoUserInfo(accessToken,response);
        return ApiResponse.ok(userInfo);
    }

    // 액세스 토큰 갱신 요청
//    @PostMapping("kakao/refresh-token")
//    public ApiResponse<String> refreshToken(@RequestParam(value = "refresh_token", required = false) String refreshToken) {
//        // 저장된 refresh_token이 없으면 요청된 refresh_token 사용
//        if (refreshToken == null || refreshToken.isEmpty()) {
//            refreshToken = tokenStore.getRefreshToken(); // tokenStore에서 저장된 refresh_token 조회
//            if (refreshToken == null) {
//                return ApiResponse.error("리프레시 토큰이 존재하지 않습니다.");
//            }
//        }
//
//        // 리프레시 토큰을 사용하여 액세스 토큰 갱신
//        String newAccessToken = kakaoService.refreshAccessToken(refreshToken);
//
//        // 새로운 refresh_token이 반환되었다면 저장
//        tokenStore.updateRefreshToken(refreshToken);
//
//        return ApiResponse.ok(newAccessToken);
//    }


}
