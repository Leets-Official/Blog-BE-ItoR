package com.blog.post.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostContentDto {
    private String contentType;  // "text" or "image"
    private String content;
    private Integer order;

    @JsonProperty("content_type")
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) {
        this.order = order;
    }
}
