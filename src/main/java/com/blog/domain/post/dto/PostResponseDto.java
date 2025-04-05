package com.blog.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.blog.domain.post.domain.ContentBlock;

public class PostResponseDto {

	private int postId;
	private int userId;
	private String title;
	private LocalDateTime createdAt;
	private List<ContentBlock> contentBlocks;
	// 댓글 리스트도 추후 추가

	public PostResponseDto(int postId, int userId, String title, LocalDateTime createdAt,
		List<ContentBlock> contentBlocks) {
		this.postId = postId;
		this.userId = userId;
		this.title = title;
		this.createdAt = createdAt;
		this.contentBlocks = contentBlocks;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public List<ContentBlock> getContentBlocks() {
		return contentBlocks;
	}

}

