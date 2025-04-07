package com.blog.kakaologin.controller;

import com.blog.kakaologin.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController//REST API 요청 처리
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService service;

    //카카오 로그인 페이지로 리디렉션
    @GetMapping("/login/kakao")
    public void redirectToKakao(HttpServletResponse resp) throws IOException {
        String clientId = "6f3be945da8d84a01dbe6bba5cfe7757";
        String redirectUri = "http://localhost:8282/login/kakao/callback";

        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri;
        resp.sendRedirect(kakaoAuthUrl); // 사용자를 카카오 로그인 페이지로 이동 시킴
    }

    //카카오에서 인가코드를 받고 처리하는 API
    @GetMapping("/login/kakao/callback")
    public String handleKakaoCallback(@RequestParam("code") String code) {
        String result = service.kakaoLogin(code); //code 값 받아서 서비스에 전달
        return result;
    }




}//class
