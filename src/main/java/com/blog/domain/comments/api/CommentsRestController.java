package com.blog.domain.comments.api;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.common.response.ResponseCode;
import com.blog.domain.comments.api.dto.request.CommentsRequest;
import com.blog.domain.comments.service.CommentsService;
import com.blog.domain.users.service.TokenService;
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
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody CommentsRequest request){

        if (authorization == null || authorization.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        int userId = tokenService.extractUserIdFromHeader(authorization);

        commentsService.addComments(userId, request);

        return ApiResponse.ok(ResponseCode.COMMENT_CREATE_SUCCESS);
    }

    // 수정
    @PatchMapping("{commentId}")
    public ApiResponse<String> updateComments(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable("commentId") int commentId,
            @RequestBody CommentsRequest request){

        if (authorization == null || authorization.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        int userId = tokenService.extractUserIdFromHeader(authorization);

        commentsService.updateComment(userId, commentId, request);

        return ApiResponse.ok(ResponseCode.COMMENT_UPDATE_SUCCESS);
    }

    // 삭제
    @DeleteMapping("{commentId}")
    public ApiResponse<String> deleteComments(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable("commentId") int commentId){

        if (authorization == null || authorization.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        int userId = tokenService.extractUserIdFromHeader(authorization);

        commentsService.deleteComment(userId, commentId);

        return ApiResponse.ok(ResponseCode.COMMENT_DELETE_SUCCESS);
    }
}
