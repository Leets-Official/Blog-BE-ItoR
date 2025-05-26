package com.blog.domain.user.controller;

import com.blog.domain.comment.dto.CommentResponse;
import com.blog.domain.comment.service.CommentService;
import com.blog.domain.post.dto.PostResponse;
import com.blog.domain.post.service.PostService;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.dto.UpdateProfileImageRequest;
import com.blog.domain.user.dto.UserProfileResponse;
import com.blog.domain.user.service.UserService;
import com.blog.global.auth.service.AuthService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
  private final AuthService authService;
  private final UserService userService;
  private final PostService postService;
  private final CommentService commentService;

  public UserController(AuthService authService, UserService userService , PostService postService , CommentService commentService) {
    this.authService = authService;
    this.userService = userService;
    this.postService = postService;
    this.commentService = commentService;
  }

  @GetMapping("/protected-resource")
  public ResponseEntity<User> getProtectedResource(
      @RequestHeader("Authorization") String token) {
    User user = authService.getUserInfoFromToken(token);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/me")
  public ResponseEntity<UserProfileResponse> getMyInfo(
      @RequestHeader("Authorization") String token) {
    User user = getUserFromToken(token);
    return ResponseEntity.ok(UserProfileResponse.from(user));
  }

  @DeleteMapping("/me")
  public ResponseEntity<Map<String, String>> deleteMyAccount(
      @RequestHeader("Authorization") String token) {
    User user = getUserFromToken(token);
    userService.deleteByEmail(user.getEmail());

    Map<String, String> response = Map.of("message", "회원 탈퇴가 완료되었습니다.");
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/me/profile-image")
  public ResponseEntity<Map<String, String>> updateProfileImage(
      @RequestHeader("Authorization") String token,
      @RequestBody UpdateProfileImageRequest request) {
    User user = getUserFromToken(token);
    userService.updateProfileImage(user.getEmail(), request.getProfileImageUrl());

    Map<String, String> response = new HashMap<>();
    response.put("message", "프로필 이미지가 성공적으로 변경되었습니다.");
    response.put("profileImageUrl", request.getProfileImageUrl());

    return ResponseEntity.ok(response);
  }

  private User getUserFromToken(String token) {
    User decoded = authService.getUserInfoFromToken(token);
    return userService.findUserByEmail(decoded.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  @GetMapping("/me/posts")
  public ResponseEntity<List<PostResponse>> getMyPosts(
      @RequestHeader("Authorization") String token) {
    User user = getUserFromToken(token);
    List<PostResponse> posts = postService.getPostsByAuthor(user.getId());
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/me/comments")
  public ResponseEntity<List<CommentResponse>> getMyComments(
      @RequestHeader("Authorization") String token) {
    User user = getUserFromToken(token);
    List<CommentResponse> comments = commentService.getCommentsByAuthor(user.getId());
    return ResponseEntity.ok(comments);
  }

}
