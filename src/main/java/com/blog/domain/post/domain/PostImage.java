package com.blog.domain.post.domain;

import java.time.LocalDateTime;

public class PostImage {
	private int imageId;
	private int postId;
	private String imageUrl;
	private LocalDateTime createdAt;

	public PostImage() {}

	public PostImage(int imageId, int postId, String imageUrl, LocalDateTime createdAt) {
		this.imageId = imageId;
		this.postId = postId;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}

	public int getPostId() {
		return postId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public int getImageId() {
		return imageId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

}
