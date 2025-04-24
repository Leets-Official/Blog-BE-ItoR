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

	public boolean updateComment(int userId, int commentId, CommentRequestDto req) {
		Optional<Comment> opt = commentRepository.findById(commentId);
		if (opt.isEmpty() || opt.get().getUserId() != userId) {
			return false;
		}
		Comment orig = opt.get();
		LocalDateTime now = LocalDateTime.now();
		Comment updated = new Comment(
			commentId,
			userId,
			orig.getPostId(),
			req.content(),
			orig.getCreatedAt(),
			now,
			orig.getDeletedAt()
		);
		return commentRepository.update(updated);
	}

	public boolean deleteComment(int userId, int commentId) {
		Optional<Comment> opt = commentRepository.findById(commentId);
		if (opt.isEmpty() || opt.get().getUserId() != userId) {
			return false;
		}
		return commentRepository.delete(commentId);
	}

	public List<CommentResponseDto> getCommentsByPost(int postId) {
		return commentRepository.findByPostId(postId).stream()
			.map(c -> {
				User u = userService.findById(c.getUserId());
				return new CommentResponseDto(
					c.getCommentId(),
					u.getNickname(),
					u.getProfileImageUrl(),
					c.getContent(),
					c.getCreatedAt().toLocalDate()
				);
			})
			.collect(Collectors.toList());
	}
}
