package com.blog.domain.comment.controller;

import com.blog.domain.comment.dto.CommentRequest;
import com.blog.domain.comment.dto.CommentResponse;
import com.blog.domain.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {
  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> createComment(@RequestBody CommentRequest request) {
    commentService.createComment(request);
    Map<String, String> response = new HashMap<>();
    response.put("message", "댓글 생성 성공");
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CommentResponse> getComment(@PathVariable UUID id) {
    return ResponseEntity.ok(commentService.getComment(id));
  }

  @GetMapping("/post/{postId}")
  public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable UUID postId) {
    return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, String>> updateComment(@PathVariable UUID id, @RequestBody CommentRequest request) {
    commentService.updateComment(id, request);
    Map<String, String> response = new HashMap<>();
    response.put("message", "댓글 수정 성공");
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deleteComment(@PathVariable UUID id) {
    commentService.deleteComment(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "댓글 삭제 성공");
    return ResponseEntity.ok(response);
  }
}
