package com.blog.workspace.adapter.out.oauth.kakao;

import com.blog.workspace.adapter.out.oauth.OAuthUserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KaKaoUserInfo implements OAuthUserInfo {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoAccount(
            @JsonProperty("email") String email,
            @JsonProperty("profile") Profile profile
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Profile(
            @JsonProperty("nickname") String nickname,
            @JsonProperty("profile_image_url") String imageUrl
    ) {
    }

    @Override
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.email() : null;
    }

    @Override
    public String getUserName() {
        return (kakaoAccount != null && kakaoAccount.profile() != null) ? kakaoAccount.profile().nickname() : null;
    }

    @Override
    public String getImageUrl() {
        return (kakaoAccount != null && kakaoAccount.profile() != null) ? kakaoAccount.profile().imageUrl() : null;
    }
}
