package com.blog.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.blog.domain.post.domain.ContentBlock;

public class PostResponseDto {

	private int postId;
	private String nickname; // User 도메인에서 조회
	private LocalDateTime createdAt;
	private int commentCount;
	private String title;
	private List<ContentBlock> contentBlocks;
	// 댓글 리스트도 추후 추가

	public PostResponseDto(int postId, String nickname, LocalDateTime createdAt, int commentCount, String title,
		List<ContentBlock> contentBlocks) {
		this.postId = postId;
		this.nickname = nickname;
		this.createdAt = createdAt;
		this.commentCount = commentCount;
		this.title = title;
		this.contentBlocks = contentBlocks;
	}

	public int getPostId() {
		return postId;
	}

	public String getNickname() {
		return nickname;
	}

	public int getCommentCount() {
		return commentCount;
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

