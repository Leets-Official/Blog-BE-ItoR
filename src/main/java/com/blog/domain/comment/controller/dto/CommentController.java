package com.blog.domain.comment.controller.dto;


import com.blog.domain.comment.controller.dto.request.CommentRequest;
import com.blog.domain.comment.controller.dto.request.CommentUpdatedRequest;
import com.blog.domain.comment.service.CommentService;
import com.blog.domain.post.service.PostService;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.aop.GetUserId;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    // 댓글 등록
    @PostMapping
    public ApiResponse<String> registerComment(@GetUserId Long userId, @Valid @RequestBody CommentRequest request) {
        commentService.registerComment(request, userId);
        return ApiResponse.ok("정상적으로 등록되었습니다.");
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<String> updateComment(@GetUserId Long userId, @PathVariable Long commentId, @Valid @RequestBody CommentUpdatedRequest request) {
        commentService.updateComment(userId, commentId, request);
        return ApiResponse.ok("정상적으로 수정되었습니다.");

    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(@GetUserId Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ApiResponse.ok("정상적으로 삭제되었습니다.");
    }

}

