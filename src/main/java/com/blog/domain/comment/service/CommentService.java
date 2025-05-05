package com.blog.domain.comment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.blog.domain.comment.domain.Comment;
import com.blog.domain.comment.dto.CommentRequestDto;
import com.blog.domain.comment.dto.CommentResponseDto;
import com.blog.domain.comment.repository.CommentRepository;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.service.UserService;
import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;

@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserService userService;

	public CommentService(CommentRepository commentRepository, UserService userService) {
		this.commentRepository = commentRepository;
		this.userService = userService;
	}

	public void addComment(int userId, int postId, CommentRequestDto req) {
		LocalDateTime now = LocalDateTime.now();
		Comment c = new Comment(0, userId, postId,
			req.content(), now, now, null);
		commentRepository.save(c);
	}

	public void updateComment(int loginUserId, int commentId, CommentRequestDto req) {
		Comment c = commentRepository.findById(commentId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMENT_NOT_FOUND));

		if (c.getUserId() != loginUserId)
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);

		Comment updated = new Comment(
			commentId, loginUserId, c.getPostId(),
			req.content(), c.getCreatedAt(), LocalDateTime.now(), c.getDeletedAt());

		commentRepository.update(updated);
	}

	public void deleteComment(int loginUserId, int commentId) {
		Comment c = commentRepository.findById(commentId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMENT_NOT_FOUND));

		if (c.getUserId() != loginUserId)
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);

		commentRepository.delete(commentId);
	}

	public List<CommentResponseDto> getCommentsByPost(int postId) {
		return commentRepository.findByPostId(postId).stream()
			.map(c -> {
				String nickname = userService.findNicknameByUserId(c.getUserId());
				String profileUrl = userService.findProfileImageUrlByUserId(c.getUserId());
				return new CommentResponseDto(
					c.getCommentId(),
					nickname,
					profileUrl,
					c.getContent(),
					c.getCreatedAt().toLocalDate()
				);
			})
			.collect(Collectors.toList());
	}
}
