package com.blog.workspace.adapter.in.web.dto.request;

import com.blog.workspace.domain.post.ContentType;

public class ContentRequest {

    private ContentType type;
    private String content;

    /// @Getter
    public ContentType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
