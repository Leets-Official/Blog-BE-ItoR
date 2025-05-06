package com.blog.domain.comment.service;

import com.blog.domain.comment.controller.request.CommentRequest;
import com.blog.domain.comment.controller.request.CommentUpdatedRequest;
import com.blog.domain.comment.controller.response.CommentResponse;
import com.blog.domain.comment.domain.Comment;
import com.blog.domain.comment.repository.CommentRepository;
import com.blog.domain.post.repository.PostRepository;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.blog.global.exception.ErrorCode.ACCESS_DENY;
import static com.blog.global.exception.ErrorCode.POST_NOT_FOUND;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 댓글 등록
    @Transactional
    public void registerComment(@Valid CommentRequest request, Long userId) {
        postRepository.findById(request.postId())
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        Comment comment = Comment.of(userId, request);
        commentRepository.save(comment);

        postRepository.incrementCommentCount(request.postId());
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long userId, Long commentId, CommentUpdatedRequest request) {
        Comment comment = getAuthorizedComment(userId, commentId);
        comment.updateContent(request.content());
        commentRepository.update(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getAuthorizedComment(userId, commentId);
        int count = commentRepository.deleteByCommentId(commentId);
        postRepository.decreaseCommentCount(comment.getPostId(), count);
    }

    // 모든 댓글 삭제
    @Transactional
    public void deleteAllCommentsByPostId(Long postId) {
        commentRepository.deleteAllByPostId(postId);
    }

    // 댓글 조회
    public Comment getCommentsByCommentId(Long commentId) {
        return commentRepository.findByCommentId(commentId);
    }

    // 댓글 전체 조회
    public List<CommentResponse> getAllCommentsByPostId(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        return commentList.stream()
                .map(comment -> {
                    Optional<User> commentUser = userRepository.findByUserId(comment.getUserId());
                    // 댓글 유저 정보와 함께 response생성
                    return CommentResponse.from(comment, commentUser.get().getNickname(), commentUser.get().getProfileImage());
                })
                .toList();
    }

    // 댓글 조회 + 권한 검증 메서드 추가
    private Comment getAuthorizedComment(Long userId, Long commentId) {
        Comment comment = getCommentsByCommentId(commentId);
        if (!comment.getUserId().equals(userId)) {
            throw new CustomException(ACCESS_DENY);
        }
        return comment;
    }

}
