package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.security.anotation.RequestUserId;
import com.blog.workspace.adapter.in.web.dto.request.PostRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostUpdateRequest;
import com.blog.workspace.adapter.in.web.dto.response.PostDetailResponse;
import com.blog.workspace.adapter.in.web.dto.response.PostListResponse;
import com.blog.workspace.application.in.post.PostUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostUseCase postService;

    public PostController(PostUseCase postService) {
        this.postService = postService;
    }

    // 게시글 작성
    @PostMapping
    ApiResponse<String> createPost(@RequestUserId Long userId, @RequestBody @Valid PostRequest postRequest) {

        postService.savePost(postRequest, userId);

        return ApiResponse.created("글 작성이 완료 되었습니다.");
    }

    // 게시글 상세 조회
    @GetMapping("/list/{postId}")
    ApiResponse<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.loadPostById(postId);

        return ApiResponse.ok(response);
    }

    @GetMapping("/list")
    ApiResponse<Page<PostListResponse>> getPostList(@RequestParam(required = false, defaultValue = "1") Integer page ,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam Long userId) {

        Pageable pageable = Pageable.of(page, size);
        Page<PostListResponse> result = postService.loadPosts(pageable, userId);

        return ApiResponse.ok(result);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    ApiResponse<String> updatePost(@RequestUserId Long userId,  @PathVariable Long postId, @RequestBody PostUpdateRequest request) {

        postService.updatePost(postId, userId, request);

        return ApiResponse.ok("글이 정상적으로 수정되었습니다.");
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    ApiResponse<String> deletePost(@RequestUserId Long userId,@PathVariable Long postId) {

        postService.deletePost(userId, postId);

        return ApiResponse.ok("정상적으로 삭제되었습니다.");
    }
}
