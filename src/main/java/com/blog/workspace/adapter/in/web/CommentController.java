package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.security.anotation.RequestUserId;
import com.blog.workspace.adapter.in.web.dto.request.CommentRequest;
import com.blog.workspace.adapter.in.web.dto.request.CommentUpdateRequest;
import com.blog.workspace.application.in.comment.CommentUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentUseCase commentService;

    public CommentController(CommentUseCase commentService) {
        this.commentService = commentService;
    }

    /// 댓글 등록
    @PostMapping
    public ApiResponse<String> createComment(@RequestUserId Long userId, @RequestBody @Valid CommentRequest request) {

        commentService.saveComment(request, userId);

        return ApiResponse.created("댓글이 등록되었습니다.");
    }

    /// 댓글 조회는 게시글과 함께 이루어지게 됩니다.

    /// 댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<String> updateComment(@RequestUserId Long userId,@PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequest request) {

        commentService.updateComment(commentId, userId, request.content());

        return ApiResponse.ok("댓글이 수정되었습니다.");
    }

    /// 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(@RequestUserId Long userId, @PathVariable Long commentId) {

        commentService.deleteComment(commentId, userId);

        return ApiResponse.ok("댓글이 삭제 되었습니다.");
    }

}
