package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.request.PostRequest;
import com.blog.workspace.adapter.in.web.dto.response.PostDetailResponse;
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
    ApiResponse<String> createPost(@RequestBody @Valid PostRequest postRequest) {
        postService.savePost(postRequest);

        return ApiResponse.created("글 작성이 완료 되었습니다.");
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    ApiResponse<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.loadPostById(postId);

        return ApiResponse.ok(response);
    }


    // 게시글 수정


    // 게시글 삭제
}
