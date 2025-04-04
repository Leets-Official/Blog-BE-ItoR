package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.request.PostRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostUpdateRequest;
import com.blog.workspace.adapter.in.web.dto.response.PostDetailResponse;
import com.blog.workspace.application.in.post.PostUseCase;
import com.blog.workspace.application.service.TokenService;
import com.blog.workspace.domain.post.Post;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostUseCase postService;
    private final TokenService tokenService;

    public PostController(PostUseCase postService, TokenService tokenService) {
        this.postService = postService;
        this.tokenService = tokenService;
    }

    // 게시글 작성
    @PostMapping
    ApiResponse<String> createPost(HttpServletRequest request, @RequestBody @Valid PostRequest postRequest) {

        /// 헤더에서 userId 가져오기 --> AOP 쓰면 좋을 듯 ...
        Long userId = tokenService.getUserIdFromToken(request);

        postService.savePost(postRequest, userId);

        return ApiResponse.created("글 작성이 완료 되었습니다.");
    }

    // 게시글 상세 조회
    @GetMapping("/list/{postId}")
    ApiResponse<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.loadPostById(postId);

        return ApiResponse.ok(response);
    }

//    @GetMapping("/list/{userId}")
//    ApiResponse<List<PostDetailResponse>> getPostList(@PathVariable Long userId) {
//        postService.loadPosts(,userId);
//    }

    // 게시글 수정
    @PutMapping("/{postId}")
    ApiResponse<String> updatePost(HttpServletRequest httpServletRequest, @PathVariable Long postId, @RequestBody PostUpdateRequest request) {

        /// 헤더에서 userId 가져오기
        Long userId = tokenService.getUserIdFromToken(httpServletRequest);

        Post post = postService.updatePost(postId, userId, request);
        log.info(post.getTitle());
        return ApiResponse.ok("글이 정상적으로 수정되었습니다.");
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    ApiResponse<String> deletePost(HttpServletRequest httpServletRequest, @PathVariable Long postId) {

        /// 헤더에서 userId 가져오기
        Long userId = tokenService.getUserIdFromToken(httpServletRequest);

        postService.deletePost(userId, postId);

        return ApiResponse.ok("정상적으로 삭제되었습니다.");
    }
}
