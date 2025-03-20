package com.blog.workspace.domain.comment;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class Comment extends BaseDomain {

    private Long id;

    private final Long boardId;

    private final Long userId;

    private final Long parentId;

    private final String content;

    public Comment(Long boardId, Long userId, Long parentId, String content, LocalDateTime created, LocalDateTime updated) {

        super(created, updated);
        this.boardId = boardId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Long getBoardId() {
        return boardId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getContent() {
        return content;
    }
}
