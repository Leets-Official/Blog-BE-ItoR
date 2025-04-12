package com.blog.domain.post.controller.dto.request;

import com.blog.domain.post.domain.ContentType;
import com.blog.domain.post.domain.PostContent;

import java.util.List;
import java.util.stream.Collectors;

public record PostContentDto(
        ContentType type, // "TEXT" 또는 "IMAGE"
        String data       // 텍스트 내용 또는 이미지 URL
) {

    // postContents에서 postContentDto로 변환
    public static List<PostContentDto> from(List<PostContent> postContent) {
        return postContent.stream()
                .map(content -> new PostContentDto(
                        ContentType.valueOf(content.getType().name()),
                        content.getContent()
                ))
                .collect(Collectors.toList());
    }
    // 이미지 추출
    public static String extractThumbnailUrl(List<PostContentDto> postContents) {
        return postContents.stream()
                .filter(dto -> dto.type() == ContentType.IMAGE)
                .map(PostContentDto::data)
                .findFirst()
                .orElse(null);
    }

}
