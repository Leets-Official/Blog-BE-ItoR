package com.blog.domain.posts.api;

import com.blog.common.response.ApiResponse;
import com.blog.domain.posts.api.dto.request.PostCreateRequest;
import com.blog.domain.posts.api.dto.response.PostListResponse;
import com.blog.domain.posts.api.dto.response.PostResponse;
import com.blog.domain.posts.service.PostsService;
import com.blog.global.security.jwt.repository.TokenStore;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
public class PostsRestController {

    private final PostsService postsService;


    public PostsRestController(PostsService postsService){
        this.postsService = postsService;
    }

    // 생성
    @PostMapping()
    public ApiResponse<String> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostCreateRequest request){

        int userId = postsService.extractUserIdFromHeader(authHeader);

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

    // 삭제

}
