package com.blog.workspace.adapter.out.oauth.kakao;

import com.blog.workspace.application.out.auth.OAuthLoginParams;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KaKaoLoginParam implements OAuthLoginParams {

    private final String authorizationCode;

    public KaKaoLoginParam(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}
