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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


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
        RestTemplate restTemplate = new RestTemplate();

        // 요청 URL
        String url = KAUTH_TOKEN_URL_HOST + "/oauth/token";

        // 요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("code", code);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 본문 구성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // POST 요청 보내기
        ResponseEntity<AuthKakaoResponse> response = restTemplate.postForEntity(
                url,
                request,
                AuthKakaoResponse.class
        );

        return ApiResponse.ok(response.getBody());
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

    // 카카오에서 이름 받아오기
    public String getNameKakao(String accessToken) {
        // 요청 URL
        String url = KAUTH_USER_URL_HOST + "/v2/user/me";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);  // Authorization: Bearer {토큰}
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 엔티티 (GET 이므로 바디는 없음)
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 전송 및 응답 수신
        ResponseEntity<AuthKaKaoUserResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                AuthKaKaoUserResponse.class
        );

        // 응답 파싱 및 nickname 리턴
        return response.getBody()
                .kakaoAccount()
                .profile()
                .nickname();
    }

}
