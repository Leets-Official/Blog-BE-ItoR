package com.blog.domain.comment.domain;

import java.time.LocalDateTime;

public class Comment {

	private int commentId;
	private int userId;
	private int postId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private LocalDateTime deletedAt;

	public int getCommentId() {
		return commentId;
	}

	public int getUserId() {
		return userId;
	}

	public int getPostId() {
		return postId;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

	public Comment(int commentId, int userId, int postId, String content, LocalDateTime createdAt,
		LocalDateTime modifiedAt,
		LocalDateTime deletedAt) {
		this.commentId = commentId;
		this.userId = userId;
		this.postId = postId;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.deletedAt = deletedAt;
	}
}
