package com.blog.workspace.application.domain;

import com.blog.workspace.domain.comment.Comment;

import java.time.LocalDateTime;

public class CommentFixtures {

    public static Comment stub(Long postId){
        return Comment.fromDB(1L, postId, 1L, "테스트 댓글", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    }

}
