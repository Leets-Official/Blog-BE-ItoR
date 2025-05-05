package com.blog.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.blog.domain.comment.dto.CommentResponseDto;
import com.blog.domain.post.domain.ContentBlock;

public class PostDetailResponseDto {

	private int postId;
	private String nickname;
	private LocalDateTime createdAt;
	private int commentCount;
	private String title;
	private List<ContentBlock> contentBlocks;
	private List<CommentResponseDto> comments;

	public PostDetailResponseDto(int postId, String nickname, LocalDateTime createdAt, int commentCount, String title,
		List<ContentBlock> contentBlocks, List<CommentResponseDto> comments) {
		this.postId = postId;
		this.nickname = nickname;
		this.createdAt = createdAt;
		this.commentCount = commentCount;
		this.title = title;
		this.contentBlocks = contentBlocks;
		this.comments = comments;
	}

	public int getPostId() {
		return postId;
	}

	public String getNickname() {
		return nickname;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public String getTitle() {
		return title;
	}

	public List<ContentBlock> getContentBlocks() {
		return contentBlocks;
	}

	public List<CommentResponseDto> getComments() {
		return comments;
	}

}
