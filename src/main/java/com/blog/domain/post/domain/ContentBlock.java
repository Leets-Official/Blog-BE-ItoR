package com.blog.domain.post.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentBlock {
	private String type;
	private String value;

	public ContentBlock() {
	}

	// 객체로 역직렬화 하기 위함
	@JsonCreator
	public ContentBlock(@JsonProperty("type") String type, @JsonProperty("value") String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	// 의도된 수정만 허용하기 위함
	public void updateValue(String newValue) {
		this.value = newValue;
	}

	public void updateType(String newType) {
		this.type = newType;
	}

}
