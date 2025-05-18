package com.blog.domain.post.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PostRequest(
        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        String title,

        @NotEmpty(message = "내용 리스트는 비어 있으면 안 됩니다.")
        List<@Valid PostContentDto> contents
) {}


