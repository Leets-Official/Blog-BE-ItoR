package com.blog.workspace.application.out.comment;

import com.blog.workspace.domain.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface LoadCommentPort {

    /// 조회 기능
    Optional<Comment> loadComment(Long commentId);

    // 게시글에 맞는 댓글 가져오기
    List<Comment> loadCommentsByPostId(Long postId);

    // 댓글 작성자 여부 확인
    boolean checkCommentByUserId(Long userId, Long commentId);
}
