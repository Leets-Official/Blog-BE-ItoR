package com.blog.comment.record;

public record CommentRecord(

        int commentNum,   // 댓글 식별자 (PK)
        String memberId,      // 작성자 아이디 (FK to MEMBER.id)
        int postId,       // 게시글 번호 (FK to POST.post_num)
        String content,       // 댓글 내용
        String createdAt,     // 생성일 (TIMESTAMP -> 문자열로 처리)
        String updateAt,      // 수정일 (TIMESTAMP -> 문자열로 처리)
        boolean isDeleted     // 삭제 여부

) { }//record

/*
- created_at, update_at은 단순 JSON 변환을 위해 String으로 처리 (타입 변환은 Mapper에서 핸들링)
- is_deleted: true면 삭제됨, false면 유지됨 (소프트 삭제 방식)
* */
