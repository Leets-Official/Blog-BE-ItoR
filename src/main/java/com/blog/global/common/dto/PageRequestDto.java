package com.blog.global.common.dto;

public class PageRequestDto {

	private int page; //나타낼 페이지
	private int size; // 페이지에 담을 글의 수

	public PageRequestDto(int page, int size) {
		this.page = 1;
		this.size = 10;
	}

	public int getPage() {
		return Math.max(1,page);
	}

	public int getSize() {
		return Math.min(Math.max(1,size), 10);
	}

	@Override
	public String toString() {
		return "PageRequestDto{" +
			"page=" + page +
			", size=" + size +
			'}';
	}

}
