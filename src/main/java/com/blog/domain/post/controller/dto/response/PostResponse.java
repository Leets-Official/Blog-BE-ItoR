package com.blog.domain.post.controller.dto.response;

import com.blog.domain.comment.controller.response.CommentResponse;
import com.blog.domain.post.controller.dto.request.PostContentDto;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.domain.PostContent;
import com.blog.domain.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

import static com.blog.domain.post.controller.dto.response.AuthorSummary.toAuthorSummary;

public record PostResponse(
        long postId,
        String title,
        List<PostContentDto> postContents,
        int commentCount,
        boolean isOwner,

        String imageUrl,
        LocalDateTime createdAt,
        AuthorSummary authorSummary,
        List<CommentResponse> comments

) {
    public static PostResponse from(Post post, List<PostContent> postContent, User user, boolean isOwner) {
        List<PostContentDto> postContents = PostContentDto.from(postContent);
        String thumbnailUrl = PostContentDto.extractThumbnailUrl(postContents);
        AuthorSummary authorSummary = toAuthorSummary(user);
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                postContents,
                post.getCommentCount(),
                isOwner,
                thumbnailUrl,
                post.getCreatedAt(),
                authorSummary,
                null
        );
    }
    public static PostResponse withComments(PostResponse from, List<CommentResponse> comments) {
        return new PostResponse(
                from.postId(),
                from.title(),
                from.postContents(),
                from.commentCount(),
                from.isOwner(),
                from.imageUrl(),
                from.createdAt(),
                from.authorSummary(),
                comments
        );
    }

}
