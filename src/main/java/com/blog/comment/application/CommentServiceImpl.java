package com.blog.comment.application;

import com.blog.comment.domain.Comment;
import com.blog.comment.domain.CommentRepository;
import com.blog.comment.domain.CommentService;
import com.blog.comment.presentation.dto.CommentCreateRequest;
import com.blog.comment.presentation.dto.CommentUpdateRequest;
import com.blog.comment.presentation.dto.CommentResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentResponse createComment(int postId, CommentCreateRequest request, int userId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Comment comment = new Comment(
                0,
                postId,
                userId,
                request.getContent(),
                now,
                null
        );

        commentRepository.save(comment);

        return toResponse(comment);
    }

    @Override
    public List<CommentResponse> getComments(int postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse updateComment(int postId, int commentId, CommentUpdateRequest request, int userId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        commentRepository.update(commentId, postId, userId, request.getContent(), now);

        Comment updated = commentRepository.findById(postId, commentId);
        return toResponse(updated);
    }

    @Override
    public void deleteComment(int postId, int commentId, int userId) {
        commentRepository.delete(postId, commentId, userId);
    }

    private CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setPostId(comment.getPostId());
        response.setUserId(comment.getUserId());
        response.setContent(comment.getContent());

        if (comment.getCreatedAt() != null) {
            response.setCreatedAt(comment.getCreatedAt().toString());
        }
        if (comment.getUpdatedAt() != null) {
            response.setUpdatedAt(comment.getUpdatedAt().toString());
        }
        return response;
    }
}
