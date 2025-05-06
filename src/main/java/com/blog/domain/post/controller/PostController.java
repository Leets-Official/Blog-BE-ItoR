package com.blog.domain.post.controller;

import com.blog.domain.post.controller.request.PostRequest;
import com.blog.domain.post.controller.response.PostListResponse;
import com.blog.domain.post.controller.response.PostResponse;
import com.blog.domain.post.service.PostService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.aop.GetUserId;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 글 등록
    @PostMapping
    public ApiResponse<String> createPost(@GetUserId Long userId, @Valid @RequestBody PostRequest postRequest) {
        postService.createPost(userId, postRequest);
        return ApiResponse.ok("정상적으로 등록되었습니다.");
    }

    // 글 목록 조회
    @GetMapping("/list")
    public ApiResponse<List<PostListResponse>> getPostList(@GetUserId Long userId) {
        List<PostListResponse> posts = postService.getPostList(userId);
        return ApiResponse.ok(posts);
    }

    // 글 상세 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@GetUserId Long userId, @PathVariable Long postId) {
        PostResponse post = postService.getPost(userId, postId);
        return ApiResponse.ok(post);
    }

    //글 수정
    @PutMapping("/{postId}")
    public ApiResponse<String> updatePost(@GetUserId Long userId, @PathVariable Long postId, @Valid @RequestBody PostRequest postRequest) {
        postService.updatePost(userId, postId, postRequest);
        return ApiResponse.ok("정상적으로 수정되었습니다.");

    }


    //글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(@GetUserId Long userId, @PathVariable Long postId) {
        postService.deletePost(userId, postId);
        return ApiResponse.ok("정상적으로 삭제되었습니다.");

    }

}
