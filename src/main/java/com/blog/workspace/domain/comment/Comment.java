package com.blog.workspace.domain.comment;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class Comment extends BaseDomain {

    private Long id;

    private final Long boardId;

    private final Long userId;

    private final Long parentId;

    private final String content;

    /// 생성자
    private Comment(Long id, Long boardId, Long userId, Long parentId, String content, LocalDateTime created, LocalDateTime updated) {

        super(created, updated);this.id = id;
        this.boardId = boardId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }

    /// 정적 팩토리 메서드
    public static Comment of(Long boardId, Long userId, Long parentId, String content, LocalDateTime created, LocalDateTime updated){
        return new Comment(null, boardId, userId, parentId, content, created, updated);
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
