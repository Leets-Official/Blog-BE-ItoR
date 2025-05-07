package com.blog.domain.comment.service;

import com.blog.domain.comment.domain.Comment;
import com.blog.domain.comment.dto.CommentRequest;
import com.blog.domain.comment.dto.CommentResponse;
import com.blog.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {
  private final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  public void createComment(CommentRequest request) {
    Comment comment = new Comment(
        UUID.randomUUID(),
        request.getContent(),
        request.getAuthorId(),
        request.getPostId(),
        LocalDateTime.now(),
        LocalDateTime.now()
    );
    commentRepository.save(comment);
  }

  public CommentResponse getComment(UUID id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    return CommentResponse.from(comment);
  }

  public List<CommentResponse> getCommentsByPostId(UUID postId) {
    return commentRepository.findByPostId(postId)
        .stream()
        .map(CommentResponse::from)
        .collect(Collectors.toList());
  }

  public void updateComment(UUID id, CommentRequest request) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    comment.updateContent(request.getContent());
    commentRepository.update(comment);
  }

  public void deleteComment(UUID id) {
    commentRepository.deleteById(id);
  }
}
