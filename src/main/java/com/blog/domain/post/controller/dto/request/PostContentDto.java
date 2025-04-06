package com.blog.domain.post.controller.dto.request;

import com.blog.domain.post.domain.ContentType;

public record PostContentDto(
        ContentType type, // "TEXT" 또는 "IMAGE"
        String data       // 텍스트 내용 또는 이미지 URL
) {}
