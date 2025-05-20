package com.blog.comment.record;

public record CommentDeleteRecord(

        int commentNum,   // 댓글 식별자 (PK)
        String memberId,     // 작성자 아이디 (FK to MEMBER.id)
        boolean isDeleted


) {}//record
