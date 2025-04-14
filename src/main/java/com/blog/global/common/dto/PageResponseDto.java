package com.blog.global.common.dto;

import java.util.List;

public class PageResponseDto<T> {

	private int page;
	private int size;
	private int total;
	private List<T> data;

	public PageResponseDto(int page, int size, int total, List<T> data) {
		this.page = page;
		this.size = size;
		this.total = total;
		this.data = data;
	}

	public int getPage() {
		return page;
	}

	public int getSize() {
		return size;
	}

	public int getTotal() {
		return total;
	}

	public List<T> getData() {
		return data;
	}

}

