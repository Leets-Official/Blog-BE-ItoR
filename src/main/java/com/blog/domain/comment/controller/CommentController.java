package com.blog.domain.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.domain.comment.dto.CommentRequestDto;
import com.blog.domain.comment.dto.CommentResponseDto;
import com.blog.domain.comment.service.CommentService;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.common.dto.GlobalResponseDto;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

	private final CommentService commentService;
	private final JwtUtil jwtUtil;

	public CommentController(CommentService commentService, JwtUtil jwtUtil) {
		this.commentService = commentService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping
	public ResponseEntity<GlobalResponseDto<Void>> createComment(@RequestHeader("Authorization") String bearerToken, @PathVariable int postId, @RequestBody
		CommentRequestDto commentRequestDto) {
		String userIdStr = jwtUtil.parseUserIdFromBearerToken(bearerToken);
		if (userIdStr == null) {
			return ResponseEntity
				.badRequest()
				.body(GlobalResponseDto.error("유효하지 않은 토큰입니다."));
		}
		int userId = Integer.parseInt(userIdStr);
		commentService.addComment(userId, postId, commentRequestDto);
		return ResponseEntity.ok(GlobalResponseDto.success(null));
	}

	// 댓글 목록 조회
	@GetMapping
	public ResponseEntity<GlobalResponseDto<List<CommentResponseDto>>> listComments(
		@PathVariable int postId
	) {
		List<CommentResponseDto> comments =
			commentService.getCommentsByPost(postId);
		return ResponseEntity
			.ok(GlobalResponseDto.success(comments));
	}

	// 댓글 수정
	@PutMapping("/{commentId}")
	public ResponseEntity<GlobalResponseDto<Void>> updateComment(@RequestHeader("Authorization") String bearerToken, @PathVariable                    int postId, @PathVariable int commentId, @RequestBody CommentRequestDto commentRequestDto
	) {
		String userIdStr = jwtUtil.parseUserIdFromBearerToken(bearerToken);
		if (userIdStr == null) {
			return ResponseEntity
				.badRequest()
				.body(GlobalResponseDto.error("유효하지 않은 토큰입니다."));
		}
		int userId = Integer.parseInt(userIdStr);
		boolean ok = commentService.updateComment(userId, commentId, commentRequestDto);
		if (!ok) {
			return ResponseEntity
				.status(403)
				.body(GlobalResponseDto.error("댓글 수정 권한이 없습니다."));
		}
		return ResponseEntity.ok(GlobalResponseDto.success(null));
	}

	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	public ResponseEntity<GlobalResponseDto<Void>> deleteComment(
		@RequestHeader("Authorization") String bearerToken,
		@PathVariable int postId,
		@PathVariable int commentId
	) {
		String userIdStr = jwtUtil.parseUserIdFromBearerToken(bearerToken);
		if (userIdStr == null) {
			return ResponseEntity
				.badRequest()
				.body(GlobalResponseDto.error("유효하지 않은 토큰입니다."));
		}
		int userId = Integer.parseInt(userIdStr);
		boolean ok = commentService.deleteComment(userId, commentId);
		if (!ok) {
			return ResponseEntity
				.status(403)
				.body(GlobalResponseDto.error("댓글 삭제 권한이 없습니다."));
		}
		return ResponseEntity.ok(GlobalResponseDto.success(null));
	}
}
