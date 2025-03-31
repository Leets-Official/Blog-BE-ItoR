package com.blog.domain.auth.api.dto.response;

public record AuthKaKaoUserResponse(
        KakaoAccount kakao_account
) {
    public record KakaoAccount(Profile profile) {
        public record Profile(String nickname) {}
    }
}
