package com.blog.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoLoginRequest {
    @JsonProperty("kakao_token")
    private String kakaoToken;

    public String getKakaoToken() { return kakaoToken; }
    public void setKakaoToken(String kakaoToken) { this.kakaoToken = kakaoToken; }
}
