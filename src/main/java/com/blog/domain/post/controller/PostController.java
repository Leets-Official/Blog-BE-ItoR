package com.blog.domain.post.controller;

import com.blog.domain.post.dto.PostRequest;
import com.blog.domain.post.dto.PostResponse;
import com.blog.domain.post.service.PostService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {
  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> createPost(@RequestBody PostRequest request) {
    postService.createPost(request);
    Map<String, String> response = new HashMap<>();
    response.put("message", "게시물 생성 성공");
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<PostResponse>> getAllPosts() {
    return ResponseEntity.ok(postService.getAllPosts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> getPost(@PathVariable UUID id) {
    return ResponseEntity.ok(postService.getPostById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, String>> updatePost(@PathVariable UUID id, @RequestBody PostRequest request) {
    postService.updatePost(id, request);
    Map<String, String> response = new HashMap<>();
    response.put("message", "게시물 수정 성공");
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deletePost(@PathVariable UUID id) {
    postService.deletePost(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "게시물 삭제 성공");
    return ResponseEntity.ok(response);
  }
}
