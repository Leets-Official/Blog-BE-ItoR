package com.blog.workspace.adapter.out.jdbc.comment;

import com.blog.workspace.domain.comment.Comment;

import java.time.LocalDateTime;

public class CommentJdbc {

    private Long id;
    private Long boardId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CommentJdbc(Long boardId, Long userId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardId = boardId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /// from
    public static CommentJdbc from(Comment comment) {
        return new CommentJdbc(
                comment.getBoardId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getCreated(),
                comment.getUpdated()
        );
    }

    /// toDomain
    public Comment toDomain() {
        return Comment.fromDB(id, boardId, userId, content, createdAt, updatedAt);
    }

}
