package com.blog.domain.user.service;


import com.blog.domain.user.repository.TokenStore;
import com.blog.global.exception.ErrorCode;
import com.blog.global.security.OAuthToken;
import com.blog.global.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.blog.global.exception.ErrorCode.INVALID_ACCESS_TOKEN;
import static com.blog.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final TokenService tokenService;

    @Value("${kakao.client-id}")
    private String clientId;

    public KakaoService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    //카카오 인가 코드는 10분 후 만료
    public String getAccessToken(String authorizationCode, String clientId, String redirectUri) {

        // ️ access token 요청
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<OAuthToken> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                OAuthToken.class
        );

        // OAuthToken 추출 및 저장
        OAuthToken oauthToken = response.getBody();
        if (oauthToken == null || oauthToken.getAccess_token() == null) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }
        return oauthToken.getAccess_token();
    }

    //  카카오 유저 정보 가져오기
    public Map<String, Object> getKakaoUserInfo(String accessToken, HttpServletResponse response) {
        System.out.println(accessToken);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> kakaoProfileRequest = new HttpEntity<>(headers);

        // 카카오 사용자 정보 요청
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                Map.class
        );
        Map<String, Object> userInfo = responseEntity.getBody();
        System.out.println(userInfo);

        tokenService.addCookie((String) userInfo.get("userId"), response);

        return userInfo;
    }

}