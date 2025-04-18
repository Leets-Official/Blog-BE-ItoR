package com.blog.domain.post.controller;

import com.blog.domain.comment.controller.dto.response.CommentResponse;
import com.blog.domain.comment.domain.Comment;
import com.blog.domain.comment.service.CommentService;
import com.blog.domain.post.controller.dto.request.PostRequest;
import com.blog.domain.post.controller.dto.response.PostListResponse;
import com.blog.domain.post.controller.dto.response.PostResponse;
import com.blog.domain.post.service.PostService;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
import com.blog.global.security.aop.GetUserId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
//    private final TokenService tokenService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 글 등록
    @PostMapping
    public ApiResponse<String> createPost(@GetUserId Long userId, @Valid @RequestBody PostRequest postRequest) {

        // 로그인이 되어 있지 않으면 로그인 페이지 URL 반환
        if (userId == null) {
            return ApiResponse.fail(new CustomException(ErrorCode.USER_NOT_FOUND));
        }

        postService.savePost(userId, postRequest);
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
