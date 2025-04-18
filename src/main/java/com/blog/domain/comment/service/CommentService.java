package com.blog.domain.comment.service;

import com.blog.domain.comment.controller.dto.request.CommentRequest;
import com.blog.domain.comment.controller.dto.request.CommentUpdatedRequest;
import com.blog.domain.comment.controller.dto.response.CommentResponse;
import com.blog.domain.comment.domain.Comment;
import com.blog.domain.comment.repository.CommentRepository;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.repository.PostRepository;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public void registerComment(@Valid CommentRequest request, Long userId) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 댓글 생성 및 저장
        Comment comment = Comment.of(userId, request);
        commentRepository.save(comment);

        post.incrementCommentCount(); // 내부적으로 count++ 해주는 메서드
    }

    // 댓글 수정
    public void updateComment(Long userId, Long commentId, CommentUpdatedRequest request) {
        Comment comment = getCommentsByCommentId(commentId);
        validateCommentOwner(comment, userId);
        comment.updateContent(request.content());
        commentRepository.update(comment);
    }

    // 댓글 삭제
    public int deleteComment(Long userId, Long commentId) {
        Comment comment = getCommentsByCommentId(commentId);
        validateCommentOwner(comment, userId);
        return commentRepository.deleteByPostId(commentId);
    }

    // 모든 댓글 삭제
    public int deleteAllCommentsByPostId(Long postId) {
        return commentRepository.deleteByPostId(postId);
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

    private void validateCommentOwner(Comment comment, Long userId) {
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }
    }

}
