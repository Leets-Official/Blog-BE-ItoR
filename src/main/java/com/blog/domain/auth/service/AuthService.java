package com.blog.domain.auth.service;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.request.AuthKaKaoRequest;
import com.blog.domain.auth.api.dto.response.AuthKaKaoUserResponse;
import com.blog.domain.auth.api.dto.response.AuthResponse;
import com.blog.domain.auth.api.dto.response.AuthKakaoResponse;
import com.blog.domain.login.api.dto.response.LoginResponse;
import com.blog.domain.login.service.LoginService;
import com.blog.domain.users.domain.Users;
import com.blog.domain.users.service.UsersService;
import com.blog.global.security.jwt.repository.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class AuthService {

    private final UsersService usersService;
    private final LoginService loginService;
    private String clientId;
    private final String KAUTH_TOKEN_URL_HOST;
    private final String KAUTH_USER_URL_HOST;
    private final TokenStore tokenStore;

    public AuthService(UsersService usersService, LoginService loginService, @Value("${kakao.client_id}") String clientId,
                       @Value("${kakao.token_url}") String KAUTH_TOKEN_URL_HOST, @Value("${kakao.user_url}") String KAUTH_USER_URL_HOST,
                       TokenStore tokenStore){
        this.usersService = usersService;
        this.clientId = clientId;
        this.KAUTH_TOKEN_URL_HOST = KAUTH_TOKEN_URL_HOST;
        this.KAUTH_USER_URL_HOST = KAUTH_USER_URL_HOST;
        this.loginService = loginService;
        this.tokenStore = tokenStore;
    }

    public AuthResponse addUserByEmail(AuthEmailRequest request) {
        return usersService.addUserByEmail(request);
    }

    // 카카오 토큰 얻기
    public AuthKakaoResponse getAccessTokenKakao(String code) {
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

        return response.getBody();
    }

    // 카카오에서 이름 받아오기
    public String getNameKakao(String accessToken) {
        String url = KAUTH_USER_URL_HOST + "/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<AuthKaKaoUserResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    AuthKaKaoUserResponse.class
            );

            return response.getBody()
                    .kakaoAccount()
                    .profile()
                    .nickname();
        } catch (HttpClientErrorException.Unauthorized e) {

            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (HttpClientErrorException e) {

            throw new CustomException(ErrorCode.KAKAO_API_ERROR);
        } catch (Exception e) {

            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 기존 회원 여부 확인 (있으면 로그인 처리, 없으면 에러 반환)
    public ApiResponse<LoginResponse> getUsersByKakaoToken(AuthKakaoResponse request) {
        String name = getNameKakao(request.accessToken());
        Users user = loginService.getUsersByName(name);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        tokenStore.storeRefreshToken(user.getUserId(), request.refreshToken());

        return ApiResponse.ok(new LoginResponse(request.accessToken(), request.refreshToken(), user));
    }

    // 새로운 사용자 등록
    public AuthResponse addUserByKakao(AuthKaKaoRequest request) {
        String name = getNameKakao(request.accessToken());
        Users user = usersService.addUserByKakao(request, name);

        tokenStore.storeRefreshToken(user.getUserId(), request.refreshToken());

        return new AuthResponse(true, "사용자 등록에 성공했습니다.", user.getUserId());
    }

}
