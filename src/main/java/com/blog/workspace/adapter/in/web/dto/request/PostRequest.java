package com.blog.workspace.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PostRequest {

    @NotNull(message = "글 작성을 위한 유저 정보가 필요합니다.")
    private Long userId;

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "글 작성은 필수입니다.")
    private List<ContentRequest> content;

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public List<ContentRequest> getContent() {
        return content;
    }
}
