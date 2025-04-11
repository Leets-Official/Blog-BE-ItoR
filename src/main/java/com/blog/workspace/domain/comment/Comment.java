package com.blog.workspace.domain.comment;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class Comment extends BaseDomain {

    private Long id;

    private final Long boardId;

    private final Long userId;

    private String content;

    /// 생성자
    private Comment(Long id, Long boardId, Long userId, String content, LocalDateTime created, LocalDateTime updated) {

        super(created, updated);this.id = id;
        this.boardId = boardId;
        this.userId = userId;
        this.content = content;
    }

    /// 정적 팩토리 메서드
    public static Comment of(Long boardId, Long userId,String content, LocalDateTime created, LocalDateTime updated){
        return new Comment(null, boardId, userId, content, created, updated);
    }

    /// 정적 팩토리 메서드
    public static Comment fromDB(Long id,Long boardId, Long userId, String content, LocalDateTime created, LocalDateTime updated){
        return new Comment(id, boardId, userId, content, created, updated);
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

    public String getContent() {
        return content;
    }

    /// 비즈니스 로직
    public void changeContent(String content) {
        this.content = content;
    }
}
