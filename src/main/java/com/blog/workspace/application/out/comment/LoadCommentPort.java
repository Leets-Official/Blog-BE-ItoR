package com.blog.workspace.application.out.comment;

import com.blog.workspace.domain.comment.Comment;

import java.util.List;

public interface LoadCommentPort {

    /// 조회 기능

    // 게시글에 맞는 댓글 가져오기
    List<Comment> loadCommentsByBoardId(Long postId);

    // 대댓글 가져오기
    List<Comment> loadCommentsByCommentId(String parentId);

    // 댓글 작성자 여부 확인
    boolean checkCommentByUserId(Long userId, Long commentId);
}
