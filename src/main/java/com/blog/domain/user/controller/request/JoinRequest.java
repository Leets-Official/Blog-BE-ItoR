package com.blog.domain.user.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record JoinRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        @Size(max = 10)
        String name,

        @NotBlank
        @Size(max = 20)
        String nickname,

        @NotNull
        LocalDateTime birth,

        @NotBlank
        @Size(max = 30)
        String introduction,

        String profileImage,

        String provider
) {
    public JoinRequest {
        // provider 기본값 설정
        if (provider == null) {
            provider = "email";
        }
    }
}
