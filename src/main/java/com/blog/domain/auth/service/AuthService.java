package com.blog.domain.auth.service;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.request.AuthKaKaoRequest;
import com.blog.domain.auth.api.dto.response.AuthKaKaoUserResponse;
import com.blog.domain.auth.api.dto.response.AuthResponse;
import com.blog.domain.auth.api.dto.response.AuthKakaoResponse;
import com.blog.domain.login.api.dto.request.LoginKakaoRequest;
import com.blog.domain.login.api.dto.response.LoginResponse;
import com.blog.domain.login.service.LoginService;
import com.blog.domain.users.domain.Users;
import com.blog.domain.users.service.UsersService;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class AuthService {

    private final UsersService usersService;
    private final LoginService loginService;
    private String clientId;
    private final String KAUTH_TOKEN_URL_HOST;
    private final String KAUTH_USER_URL_HOST;

    public AuthService(UsersService usersService, LoginService loginService, @Value("${kakao.client_id}") String clientId,
                       @Value("${kakao.token_url}") String KAUTH_TOKEN_URL_HOST, @Value("${kakao.user_url}") String KAUTH_USER_URL_HOST){
        this.usersService = usersService;
        this.clientId = clientId;
        this.KAUTH_TOKEN_URL_HOST = KAUTH_TOKEN_URL_HOST;
        this.KAUTH_USER_URL_HOST = KAUTH_USER_URL_HOST;
        this.loginService = loginService;
    }

    public AuthResponse addUserByEmail(AuthEmailRequest request) {
        return usersService.addUserByEmail(request);
    }

    // 카카오 토큰 얻기
    public ApiResponse<AuthKakaoResponse> getAccessTokenKakao(String code) {

        AuthKakaoResponse response = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(AuthKakaoResponse.class)
                .block();

        return ApiResponse.ok(response);
    }

    public AuthResponse addUserByKakao(AuthKaKaoRequest request){

        // 회원가입된 이메일 없으면 가입 (이메일, 이름 받아오기 - 이메일 권환 X)
        String name = getNameKakao(request.accessToken());

        return usersService.addUserByKakao(request, name);
    }

    // 이름, 이메일 조회 있으면 로그인, 없으면 회원가입 api로
    public ApiResponse<LoginResponse> getUsersByName(LoginKakaoRequest request){
        String name = getNameKakao(request.accessToken());
        Users user = loginService.getUsersByName(name);

        if (user == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return ApiResponse.ok(new LoginResponse(request.refreshToken(), request.accessToken(), user));
    }

    // 이름 받아오기 (이메일 받아오기)
    public String getNameKakao(String accessToken) {

        return WebClient.builder()
                .baseUrl(KAUTH_USER_URL_HOST)  // "https://kapi.kakao.com"
                .build()
                .get()
                .uri("/v2/user/me")  // 사용자 정보 요청 URL
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)  // Authorization 헤더에 Bearer 토큰 추가
                .retrieve()
                .bodyToMono(AuthKaKaoUserResponse.class)  // 응답을 AuthKaKaoUserResponse 객체로 매핑
                .map(response -> response.kakaoAccount().profile().nickname())  // nickname 추출
                .block();  // 동기 호출
    }
}
