package com.blog.domain.user.controller.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

public record UpdateRequest(

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "주소는 필수입니다.")
        String address,

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @Past(message = "생일은 과거 날짜여야 합니다.")
        LocalDateTime birth,

        String profileImage,

        @Size(max = 255, message = "자기소개는 255자 이하로 입력해주세요.")
        String introduction

) {}
