package com.blog.domain.post.controller.dto.response;

import com.blog.domain.post.controller.dto.request.PostContentDto;
import com.blog.domain.post.domain.ContentType;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.domain.PostContent;
import com.blog.domain.user.domain.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.blog.domain.post.controller.dto.response.AuthorSummary.toAuthorSummary;

public record PostResponse(
        Long postId,
        String title,
        List<PostContentDto> postContents,
        int commentCount,
        boolean isOwner,

        String imageUrl,
        LocalDate createdAt,
        AuthorSummary authorSummary

) {

    public static PostResponse from(Post post, List<PostContent> postContent, User user, boolean isOwner) {
        List<PostContentDto> postContents = toPostContentDtos(postContent);
        String thumbnailUrl = extractThumbnailUrl(postContents);
        AuthorSummary authorSummary = toAuthorSummary(user);

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                postContents,
                post.getCommentCount(),
                isOwner,
                thumbnailUrl,
                post.getCreatedAt(),
                authorSummary
        );
    }

    // postContents에서 postContentDto로 변환
    private static List<PostContentDto> toPostContentDtos(List<PostContent> postContent) {
        return postContent.stream()
                .map(content -> new PostContentDto(
                        ContentType.valueOf(content.getType().name()),
                        content.getContent()
                ))
                .collect(Collectors.toList());
    }

    private static String extractThumbnailUrl(List<PostContentDto> postContents) {
        return postContents.stream()
                .filter(dto -> dto.type() == ContentType.IMAGE)
                .map(PostContentDto::data)
                .findFirst()
                .orElse(null);
    }
}
