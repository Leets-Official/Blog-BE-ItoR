package com.blog.domain.auth.api.dto.request;

import java.time.LocalDate;

public record AuthKaKaoRequest(
        String accessToken,
        String refreshToken,
        String nickname,
        LocalDate birth,
        String profileImage,
        String introduce
) {
}
