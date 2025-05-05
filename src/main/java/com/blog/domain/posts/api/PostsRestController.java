package com.blog.domain.posts.api;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.posts.api.dto.request.PostsRequest;
import com.blog.domain.posts.api.dto.response.PostListResponse;
import com.blog.domain.posts.api.dto.response.PostResponse;
import com.blog.domain.posts.service.PostsService;
import com.blog.domain.users.service.TokenService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
public class PostsRestController {

    private final PostsService postsService;
    private final TokenService tokenService;


    public PostsRestController(PostsService postsService, TokenService tokenService){
        this.postsService = postsService;
        this.tokenService = tokenService;
    }

    // 생성
    @PostMapping()
    public ApiResponse<String> createPost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody PostsRequest request){

        if (authorization == null || authorization.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        int userId = tokenService.extractUserIdFromHeader(authorization);

        postsService.createPost(userId, request);

        return ApiResponse.ok("게시글이 작성 되었습니다.");
    }

    // 목록 조회
    // 페이징 추가하기
    @GetMapping("/list")
    public ApiResponse<PostListResponse> postList(){

        return ApiResponse.ok(postsService.getPostsList());
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> postBlockList(
            @PathVariable("postId") int postId){

        return ApiResponse.ok(postsService.getPostBlockListByPostId(postId));
    }

    // 수정
    @PatchMapping("/{postId}")
    public ApiResponse<String> updatePost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable("postId") int postId,
            @RequestBody PostsRequest request){

        if (authorization == null || authorization.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        int userId = tokenService.extractUserIdFromHeader(authorization);
        postsService.updatePost(userId, postId, request);

        return ApiResponse.ok("수정 성공 했습니다.");
    }

    // 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<String> updatePost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable("postId") int postId){

        if (authorization == null || authorization.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        int userId = tokenService.extractUserIdFromHeader(authorization);
        postsService.deletePost(userId, postId);

        return ApiResponse.ok("삭제 성공 했습니다.");
    }
}
