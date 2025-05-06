package com.blog.workspace.adapter.in.web.dto.request;

import java.util.List;

public class PostUpdateRequest {

    /*
        @Valid 대신 서비스 계층에서 예외처리 진행했습니다.
     */

    private String title;

    private List<ContentRequest> content;


    public PostUpdateRequest(String title, List<ContentRequest> content) {
        this.title = title;
        this.content = content;
    }

    /**@Getter
     */
    public String getTitle() {
        return title;
    }

    public List<ContentRequest> getContent() {
        return content;
    }

}
