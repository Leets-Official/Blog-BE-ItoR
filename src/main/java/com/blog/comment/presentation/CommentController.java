package com.blog.comment.presentation;

import com.blog.comment.domain.CommentService;
import com.blog.comment.presentation.dto.CommentCreateRequest;
import com.blog.comment.presentation.dto.CommentResponse;
import com.blog.comment.presentation.dto.CommentUpdateRequest;
import com.blog.token.infrastructure.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    public CommentController(CommentService commentService, JwtTokenProvider jwtTokenProvider) {
        this.commentService = commentService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable int postId,
            @RequestBody CommentCreateRequest request,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Integer userId = extractUserIdFromToken(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        CommentResponse response = commentService.createComment(postId, request, userId);
        return ResponseEntity.ok(response);
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable int postId) {
        List<CommentResponse> responses = commentService.getComments(postId);
        return ResponseEntity.ok(responses);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable int postId,
            @PathVariable int commentId,
            @RequestBody CommentUpdateRequest request,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Integer userId = extractUserIdFromToken(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        CommentResponse response = commentService.updateComment(postId, commentId, request, userId);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable int postId,
            @PathVariable int commentId,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Integer userId = extractUserIdFromToken(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        commentService.deleteComment(postId, commentId, userId);
        return ResponseEntity.ok().build();
    }

    private Integer extractUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) return null;
        String rawToken = token.substring(7);
        return jwtTokenProvider.extractUserId(rawToken);
    }
}
