package com.blog.domain.user.service;


import com.blog.domain.user.repository.TokenStore;
import com.blog.global.exception.ErrorCode;
import com.blog.global.security.OAuthToken;
import com.blog.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenStore tokenStore;
    private final TokenService tokenService;

    @Value("${kakao.client-id}")
    private String clientId;

    public KakaoService(TokenStore tokenStore, TokenService tokenService) {
        this.tokenStore = tokenStore;
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

        headers.setBearerAuth(accessToken); // 토큰을 Bearer 형태로 전달
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> kakaoProfileRequest = new HttpEntity<>(headers);

        // 카카오 사용자 정보 요청
        ResponseEntity<Map> responseEntity  = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                Map.class
        );
        Map<String, Object> userInfo = responseEntity.getBody();
        System.out.println(userInfo);

        tokenService.addCookie((String) userInfo.get("userId"), response);

        if (userInfo == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        return userInfo;
    }

    // 카카오에서 액세스 토큰이 유효한지 확인하는 로직
    public boolean isAccessTokenValid(String accessToken) {
        try {
            // 카카오 API에서 토큰 정보 검증 요청
            String url = "https://kapi.kakao.com/v1/user/access_token_info";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);  // 액세스 토큰을 Authorization 헤더에 추가
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 만약 200 OK 응답을 받았다면 토큰은 유효함
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            // 만약 401 Unauthorized 응답이 오면 토큰이 만료된 것이므로 false를 반환
            return false;
        }
    }

    // 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받는 로직
    public String refreshAccessToken(String refreshToken) {
        try {
            String url = "https://kauth.kakao.com/oauth/token";

            // 리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급받기 위한 요청 파라미터
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", clientId);  // 카카오에서 제공한 client_id
            params.add("refresh_token", refreshToken);  // 리프레시 토큰

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            // 카카오 API에 POST 요청을 보내고 응답 받기
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            // 응답에서 새로 발급받은 액세스 토큰을 추출하여 반환
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }


}