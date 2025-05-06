package com.blog.comment.record;
//댓글 생성에만 쓰임
public record CommentCreateRecord(

        String  memberId,
        int postId,
        String  content

) {}//record
