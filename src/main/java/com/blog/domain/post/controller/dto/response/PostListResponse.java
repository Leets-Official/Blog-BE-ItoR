package com.blog.domain.post.controller.dto.response;

import com.blog.domain.post.controller.dto.request.PostContentDto;
import com.blog.domain.post.domain.ContentType;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.domain.PostContent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostListResponse(
        long postId,
        String title,
        List<PostContentDto> postContents,
        int commentCount,
        String imageUrl,
        LocalDateTime createdAt
) {

    public static PostListResponse from(Post post, List<PostContent> postContents) {
        List<PostContentDto> postContentDtos = PostContentDto.from(postContents);
        String thumbnailUrl = PostContentDto.extractThumbnailUrl(postContentDtos);
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                postContentDtos,
                post.getCommentCount(),
                thumbnailUrl,
                post.getCreatedAt()
        );
    }

}
