package com.blog.domain.post.dto;

import java.util.List;

import com.blog.domain.post.domain.ContentBlock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class PostRequestDto {

	@NotBlank(message = "제목을 입력해주세요")
	private String title;

	@NotEmpty(message =  "본문 입력은 필수입니다!")
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
