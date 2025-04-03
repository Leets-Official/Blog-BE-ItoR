package com.blog.domain.post.domain;

import java.time.LocalDateTime;

public class Post {

	private int postId;
	private int userId;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Post() {
	}

	public Post(int postId, int userId, String title, String content, LocalDateTime createdAt,
		LocalDateTime modifiedAt) {
		this.postId = postId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public int getPostId() {
		return postId;
	}

	public int getUserId() {
		return userId;
	}

	public String getTitle() {
		return title;
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

}
