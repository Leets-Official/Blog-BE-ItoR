package com.blog.workspace.application.service;

import com.blog.workspace.adapter.in.web.dto.request.CommentRequest;
import com.blog.workspace.application.in.comment.CommentUseCase;
import com.blog.workspace.application.out.comment.DeleteCommentPort;
import com.blog.workspace.application.out.comment.LoadCommentPort;
import com.blog.workspace.application.out.comment.SaveCommentPort;
import com.blog.workspace.application.out.post.LoadPostPort;
import com.blog.workspace.application.service.exception.NotEqualDeleteException;
import com.blog.workspace.application.service.exception.NotEqualUpdateException;
import com.blog.workspace.domain.comment.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CommentService implements CommentUseCase {

    private final SaveCommentPort savePort;
    private final LoadCommentPort loadPort;
    private final DeleteCommentPort deletePort;

    private final LoadPostPort loadPostPort;

    public CommentService(SaveCommentPort savePort, LoadCommentPort loadPort, DeleteCommentPort deletePort, LoadPostPort loadPostPort) {
        this.savePort = savePort;
        this.loadPort = loadPort;
        this.deletePort = deletePort;
        this.loadPostPort = loadPostPort;
    }

    @Override
    public Comment saveComment(CommentRequest request, Long userId) {

        LocalDateTime now = LocalDateTime.now();

        // 댓글 객체 생성
        Comment comment = Comment.of
                (request.postId(), userId, request.content(), now, now);

        return savePort.saveComment(comment);
    }

    // 게시판에 따른 댓글 목록 조회
    @Override
    public List<Comment> loadCommentsByPost(Long postId) {

        /// 게시판 예외처리
        validatePostExists(postId);

        return loadPort.loadCommentsByPostId(postId);
    }

    @Override
    public Comment updateComment(Long commentId, Long userId, String content) {

        /// 댓글 가져오기
        Comment comment = getComment(commentId);

        /// 유저 검증 조건 처리
        if (!comment.getUserId().equals(userId)) {
            throw new NotEqualUpdateException("글 작성자가 아니기에 수정할 수 없습니다.");
        }

        ///  글 수정하기
        comment.changeContent(content);

        return savePort.updateComment(comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {

        /// 댓글 가져오기
        Comment comment = getComment(commentId);

        /// 유저 예외처리
        if (!comment.getUserId().equals(userId)) {
            throw new NotEqualDeleteException("글 작성자가 아니기에 삭제할 수 없습니다.");
        }

        /// 최종 삭제
        deletePort.deleteById(commentId);
    }


    // 내부 함수
    private void validatePostExists(Long postId) {
        loadPostPort.loadPost(postId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시판이 존재하지 않습니다."));
    }

    private Comment getComment(Long commentId) {
        return loadPort.loadComment(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 댓글이 존재하지 않습니다."));
    }



}
