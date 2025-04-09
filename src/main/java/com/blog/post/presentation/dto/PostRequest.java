package com.blog.post.presentation.dto;

import java.util.List;

public class PostRequest {
    private String title;
    private List<PostContentDto> contents;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<PostContentDto> getContents() { return contents; }
    public void setContents(List<PostContentDto> contents) {
        this.contents = contents;
    }
}
