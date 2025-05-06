package com.blog.workspace.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PostRequest {

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "글 작성은 필수입니다.")
    private List<ContentRequest> content;

    public PostRequest(String title, List<ContentRequest> content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public List<ContentRequest> getContent() {
        return content;
    }
}
