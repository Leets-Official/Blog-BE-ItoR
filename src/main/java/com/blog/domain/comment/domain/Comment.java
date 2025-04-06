package com.blog.domain.comment.domain;

import com.blog.global.common.BaseDomain;

import java.time.LocalDate;

public class Comment extends BaseDomain {
    private Long id;
    private Long user_id;
    private Long post_id;
    private String comment;

    public Comment(LocalDate updatedAt, LocalDate createdAt) {
        super(updatedAt, createdAt);
    }
}
