package com.blog.workspace.domain.comment;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class Comment extends BaseDomain {

    private Long id;

    private final Long boardId;

    private final Long userId;

    private final Long parentId;

    private final String content;

    /// 서비스 내부 생성자
    public Comment(Long boardId, Long userId, Long parentId, String content, LocalDateTime created, LocalDateTime updated) {

        super(created, updated);
        this.boardId = boardId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }

    /// JDBC에서 Domain으로 변환할 때 생성자
    public Comment(Long id, Long boardId, Long userId, Long parentId, String content, LocalDateTime created, LocalDateTime updated) {

        super(created, updated);
        this.id = id;
        this.boardId = boardId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }

    /// @Getter
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

    /// 비즈니스 로직
}
