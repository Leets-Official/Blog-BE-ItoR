package com.blog.domain.post.controller;

import com.blog.domain.post.controller.dto.request.PostRequest;
import com.blog.domain.post.controller.dto.response.PostListResponse;
import com.blog.domain.post.controller.dto.response.PostResponse;
import com.blog.domain.post.service.PostService;
import com.blog.domain.user.service.TokenService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final TokenService tokenService;

    public PostController(PostService postService, TokenService tokenService) {
        this.postService = postService;
        this.tokenService = tokenService;
    }

    // 글 등록
    @PostMapping
    public ApiResponse<String> createPost(HttpServletRequest request, @Valid @RequestBody PostRequest postRequest) {
        Long userId = tokenService.getUserIdFromCookie(request);

        // 로그인이 되어 있지 않으면 로그인 페이지 URL 반환
        if (userId == null) {
            return ApiResponse.fail(new CustomException(ErrorCode.USER_NOT_FOUND));
        }

        postService.savePost(userId, postRequest);
        return ApiResponse.ok("정상적으로 등록되었습니다.");
    }

    // 글 목록 조회
    @GetMapping("/list")
    public ApiResponse<List<PostListResponse>> getPostList(HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromCookie(request);
        List<PostListResponse> posts = postService.getPostList(userId);
        return ApiResponse.ok(posts);

    }

    // 글 상세 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(HttpServletRequest request, @PathVariable Long postId) {
        Long userId = tokenService.getUserIdFromCookie(request);
        PostResponse post = postService.getPost(userId, postId);
        return ApiResponse.ok(post);
    }

    //글 수정
    @PutMapping("/{postId}")
    public ApiResponse<String> updatePost(HttpServletRequest request, @PathVariable Long postId, @Valid @RequestBody PostRequest postRequest) {
        Long userId = tokenService.getUserIdFromCookie(request);
        postService.updatePost(userId, postId, postRequest);
        return ApiResponse.ok("정상적으로 수정되었습니다.");

    }


    //글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(HttpServletRequest request, @PathVariable Long postId) {
        Long userId = tokenService.getUserIdFromCookie(request);
        postService.deletePost(userId, postId);
        return ApiResponse.ok("정상적으로 삭제되었습니다.");

    }

}
