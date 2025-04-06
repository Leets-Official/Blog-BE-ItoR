package com.blog.domain.post.controller.dto.response;

import com.blog.domain.post.controller.dto.request.PostContentDto;
import com.blog.domain.post.domain.ContentType;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.domain.PostContent;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record PostListResponse(
        Long postId,
        String title,
        List<PostContentDto> postContents,
        int commentCount,
        String imageUrl,
        LocalDate createdAt
) {

    public static PostListResponse from(Post post, List<PostContent> postContents) {
        List<PostContentDto> postContentDtos = toPostContentDtos(postContents);
        String thumbnailUrl = extractThumbnailUrl(postContentDtos);
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                postContentDtos,
                post.getCommentCount(),
                thumbnailUrl,
                post.getCreatedAt()
        );
    }

    // postContents에서 postContentDto로 변환
    private static List<PostContentDto> toPostContentDtos(List<PostContent> postContents) {
        return postContents.stream()
                .map(content -> new PostContentDto(
                        ContentType.valueOf(content.getType().name()),
                        content.getContent()
                ))
                .collect(Collectors.toList());
    }

    // content에서 추출된 첫 이미지를 썸네일로 설정
    private static String extractThumbnailUrl(List<PostContentDto> postContents) {
        return postContents.stream()
                .filter(dto -> dto.type() == ContentType.IMAGE)
                .map(PostContentDto::data)
                .findFirst()
                .orElse(null);
    }
}
