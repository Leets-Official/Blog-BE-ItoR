package com.blog.workspace.application.out.comment;

import com.blog.workspace.domain.comment.Comment;

public interface SaveCommentPort {

    /// 저장하기

    // 저장하기
    Comment saveComment(Comment comment);

    // 값 수정하기
    Comment updateComment(Comment comment);
}
