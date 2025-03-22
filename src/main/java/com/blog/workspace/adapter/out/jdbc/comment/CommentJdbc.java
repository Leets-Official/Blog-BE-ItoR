package com.blog.workspace.adapter.out.jdbc.comment;

import com.blog.workspace.domain.comment.Comment;

import java.time.LocalDateTime;

public class CommentJdbc {

    private Long id;
    private final Long boardId;
    private final Long userId;
    private final Long parentId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentJdbc(Long boardId, Long userId, Long parentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardId = boardId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /// from
    public static CommentJdbc from(Comment comment) {
        return new CommentJdbc(
                comment.getBoardId(),
                comment.getUserId(),
                comment.getParentId(),
                comment.getContent(),
                comment.getCreated(),
                comment.getUpdated()
        );
    }

    /// toDomain
    public Comment toDomain() {
        return new Comment(
                this.id,
                this.boardId,
                this.userId,
                this.parentId,
                this.content,
                this.createdAt,
                this.updatedAt
        );
    }
}