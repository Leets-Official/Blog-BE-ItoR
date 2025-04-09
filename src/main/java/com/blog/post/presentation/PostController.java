package com.blog.post.presentation;

import com.blog.post.domain.PostService;
import com.blog.post.presentation.dto.PostRequest;
import com.blog.post.presentation.dto.PostResponse;
import com.blog.token.infrastructure.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    public PostController(PostService postService, JwtTokenProvider jwtTokenProvider) {
        this.postService = postService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostRequest request,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = jwtTokenProvider.extractUserId(token);
        PostResponse response = postService.createPost(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Integer postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Integer postId,
            @RequestBody PostRequest request,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = jwtTokenProvider.extractUserId(token);
        return ResponseEntity.ok(postService.updatePost(postId, request, userId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Integer postId,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = jwtTokenProvider.extractUserId(token);
        postService.deletePost(postId, userId);
        return ResponseEntity.ok().build();
    }
}
