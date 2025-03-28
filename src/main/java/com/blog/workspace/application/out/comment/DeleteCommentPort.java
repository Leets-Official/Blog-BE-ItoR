package com.blog.workspace.application.out.comment;

public interface DeleteCommentPort {

    /// 삭제 기능

    // 아이디를 통해 삭제하기
    void deleteById(Long commentId);
}
