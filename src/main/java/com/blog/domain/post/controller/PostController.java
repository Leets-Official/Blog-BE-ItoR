package com.blog.domain.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.domain.post.dto.PostDetailResponseDto;
import com.blog.domain.post.dto.PostRequestDto;
import com.blog.domain.post.dto.PostResponseDto;
import com.blog.domain.post.service.PostService;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.common.dto.GlobalResponseDto;
import com.blog.global.common.dto.PageRequestDto;
import com.blog.global.common.dto.PageResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;
	private final JwtUtil jwtUtil;

	public PostController(PostService postService, JwtUtil jwtUtil) {
		this.postService = postService;
		this.jwtUtil = jwtUtil;
	}

	//게시글 생성
	@PostMapping
	public ResponseEntity<GlobalResponseDto<Void>> createPost(
		@RequestHeader("Authorization") String bearerToken,
		@RequestBody @Valid PostRequestDto dto
	) {
		String userIdStr = jwtUtil.parseUserIdFromBearerToken(bearerToken);
		if (userIdStr == null) {
			return ResponseEntity.badRequest().body(GlobalResponseDto.error("유효하지 않은 토큰입니다."));
		}
		int userId = Integer.parseInt(userIdStr);
		postService.createPost(userId, dto);

		return ResponseEntity.ok(GlobalResponseDto.success(null));
	}

	//게시글 조회
	@GetMapping("/list/{postId}")
	public ResponseEntity<GlobalResponseDto<PostDetailResponseDto>> getPost(@PathVariable int postId) {
		return ResponseEntity.ok(GlobalResponseDto.success(postService.getPostById(postId)));
	}

	//게시글 목록 조회 (페이지네이션)
	@GetMapping("/list")
	public ResponseEntity<GlobalResponseDto<PageResponseDto<PostResponseDto>>> getPostPage(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		PageRequestDto pageRequest = new PageRequestDto(page, size);
		return ResponseEntity.ok(GlobalResponseDto.success(postService.getPostPage(pageRequest)));
	}

	//게시글 수정
	@PutMapping("/{postId}")
	public ResponseEntity<GlobalResponseDto<Void>> updatePost(
		@RequestHeader("Authorization") String bearerToken,
		@PathVariable int postId,
		@RequestBody @Valid PostRequestDto dto
	) {
		String userIdStr = jwtUtil.parseUserIdFromBearerToken(bearerToken);
		if (userIdStr == null) {
			return ResponseEntity.badRequest().body(GlobalResponseDto.error("유효하지 않은 토큰입니다."));
		}

		int userId = Integer.parseInt(userIdStr);
		postService.updatePost(userId, postId, dto);
		return ResponseEntity.ok(GlobalResponseDto.success(null));
	}

	//게시글 삭제
	@DeleteMapping("/{postId}")
	public ResponseEntity<GlobalResponseDto<Void>> deletePost(
		@RequestHeader("Authorization") String bearerToken,
		@PathVariable int postId
	) {
		String userIdStr = jwtUtil.parseUserIdFromBearerToken(bearerToken);
		if (userIdStr == null) {
			return ResponseEntity.badRequest().body(GlobalResponseDto.error("유효하지 않은 토큰입니다."));
		}

		int userId = Integer.parseInt(userIdStr);
		postService.deletePost(userId, postId);
		return ResponseEntity.ok(GlobalResponseDto.success(null));
	}


}
