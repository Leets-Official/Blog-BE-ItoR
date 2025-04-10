package com.blog.workspace.application.in.comment;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.workspace.adapter.in.web.dto.request.CommentRequest;
import com.blog.workspace.adapter.in.web.dto.request.CommentUpdateRequest;
import com.blog.workspace.domain.comment.Comment;

public interface CommentUseCase {

    /*
        사용자는 로그인을 하지 않고도 댓글을 확인할 수 있어야 합니다.
        사용자는 자신의 댓글만 수정, 삭제할 수 있어야 합니다.
        댓글에는 댓글을 달수 없습니다(단 원하는 경우 구현해도 괜찮습니다)
     */

    /// 댓글 작성
    Comment saveComment(CommentRequest request, Long userId);

    /// 댓글 조회
    // 게시판에 따른 댓글 목록 조회 (페이징 처리10개)
    Page<Comment> loadCommentsByPost(Pageable pageable, Long postId);

    /// 댓글 수정
    Comment updateComment(Long commentId, Long userId, CommentUpdateRequest request);

    /// 댓글 삭제
    void deleteComment(Long userId, Long commentId);

}
