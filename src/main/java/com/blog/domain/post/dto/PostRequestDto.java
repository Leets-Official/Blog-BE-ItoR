package com.blog.domain.post.dto;

import java.util.List;

import com.blog.domain.post.domain.ContentBlock;

public class PostRequestDto {

	private String title;
	private List<ContentBlock> contentBlocks;

	public PostRequestDto() {
	}

	public String getTitle() {
		return title;
	}

	public List<ContentBlock> getContentBlocks() {
		return contentBlocks;
	}

}
