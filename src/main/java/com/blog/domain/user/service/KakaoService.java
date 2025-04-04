package com.blog.domain.user.service;

import com.blog.global.security.OAuthToken;
import com.blog.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.blog.global.exception.ErrorCode.INVALID_ACCESS_TOKEN;

@Service
public class KakaoService {
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    //카카오 인가 코드는 10분 후 만료
    public String getAccessToken(String authorizationCode) {
        HttpHeaders headers = createHeaders();
        HttpEntity<MultiValueMap<String, String>> request = createTokenRequest(authorizationCode, clientId, redirectUri, headers);

        ResponseEntity<OAuthToken> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                OAuthToken.class
        );

        return extractAccessToken(response);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private HttpEntity<MultiValueMap<String, String>> createTokenRequest(String code, String clientId, String redirectUri, HttpHeaders headers) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        return new HttpEntity<>(params, headers);
    }

    private String extractAccessToken(ResponseEntity<OAuthToken> response) {
        OAuthToken oauthToken = response.getBody();
        if (oauthToken == null || oauthToken.getAccess_token() == null) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }
        return oauthToken.getAccess_token();
    }


    //  카카오 유저 정보 가져오기
    public Map<String, Object> getKakaoUserInfo(String accessToken) {
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

        return responseEntity.getBody();

    }

}
