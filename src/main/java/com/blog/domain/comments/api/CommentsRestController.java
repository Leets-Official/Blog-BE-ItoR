package com.blog.domain.comments.api;

import com.blog.common.response.ApiResponse;
import com.blog.domain.comments.api.dto.request.CommentsRequest;
import com.blog.domain.comments.domain.repository.CommentsRepository;
import com.blog.domain.comments.service.CommentsService;
import com.blog.domain.users.service.TokenService;
import org.apache.el.parser.Token;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsRestController {

    private final TokenService tokenService;
    private final CommentsService commentsService;

    public CommentsRestController(TokenService tokenService, CommentsService commentsService){
        this.tokenService = tokenService;
        this.commentsService = commentsService;
    }

    // 생성
    @PostMapping
    public ApiResponse<String> createComments(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CommentsRequest request){

        int userId = tokenService.extractUserIdFromHeader(authorization);

        commentsService.addComments(userId, request);

        return ApiResponse.ok("댓글이 작성되었습니다.");
    }

    // 수정
    @PatchMapping("{commentId}")
    public ApiResponse<String> updateComments(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("commentId") int commentId,
            @RequestBody CommentsRequest request){

        int userId = tokenService.extractUserIdFromHeader(authorization);

        commentsService.updateComment(userId, commentId, request);

        return ApiResponse.ok("댓글 수정 성공했습니다.");
    }

    // 삭제
    @DeleteMapping("{commentId}")
    public ApiResponse<String> deleteComments(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("commentId") int commentId){

        int userId = tokenService.extractUserIdFromHeader(authorization);

        commentsService.deleteComment(userId, commentId);

        return ApiResponse.ok("댓글 삭제 성공했습니다.");
    }
}
