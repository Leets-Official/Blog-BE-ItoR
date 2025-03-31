package com.blog.domain.auth.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    // 카카오 동의 화면
    @GetMapping("/kakao")
    public void loginPage(HttpServletResponse response) throws IOException {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + client_id + "&redirect_uri=" + redirect_uri;

        response.sendRedirect(location);
    }

}
