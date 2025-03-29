package com.blog.workspace.adapter.out.oauth.kakao;

import com.blog.workspace.adapter.out.oauth.OAuthUserInfo;
import com.blog.workspace.application.in.auth.AuthUserUseCase;
import com.blog.workspace.application.out.auth.AuthPort;
import com.blog.workspace.application.out.auth.OAuthLoginParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class KaKaoApiClient implements AuthPort {

    private static final String GRANT_TYPE = "authorization_code";
    private static final Logger log = LoggerFactory.getLogger(KaKaoApiClient.class);

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;
    @Value("${oauth.kakao.url.api}")
    private String apiUrl;
    @Value("${oauth.kakao.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    public KaKaoApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * https://kauth.kakao.com/oauth/token?
     * grant_type=authorization_code (추가할 param)
     * &client_id=${REST_API_KEY} (추가할 param)
     * &redirect_uri=${REDIRECT_URI} (KaKaoLoginParams에 포함되어 있음)
     * &code=${code} (KaKaoLoginParams에 포함되어 있음)
     */

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";
        HttpEntity<MultiValueMap<String, String>> request = generateHttpRequest(params);

        KaKaoToken kaKaoToken = restTemplate.postForObject(url, request, KaKaoToken.class);
        Objects.requireNonNull(kaKaoToken);
        return kaKaoToken.accessToken();
    }

    @Override
    public OAuthUserInfo requestOAuthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String requestToken = "Bearer " + accessToken;
        httpHeaders.set("Authorization", requestToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile.nickname\", \"kakao_account.profile.profile_image_url\"]");
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        return restTemplate.postForObject(url, request, KaKaoUserInfo.class);
    }

    /// 내부 함수
    private HttpEntity<MultiValueMap<String, String>> generateHttpRequest(OAuthLoginParams params) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        return new HttpEntity<>(body, httpHeaders);
    }
}
