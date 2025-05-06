package com.blog.workspace.adapter.in.web.dto.request;

import com.blog.workspace.domain.post.ContentType;
import org.springframework.web.multipart.MultipartFile;

public class ContentRequest {

    private ContentType type;
    private String content;
    private MultipartFile image;

    public ContentRequest(ContentType type, String content, MultipartFile image) {
        this.type = type;
        this.content = content;
        this.image = image;
    }

    /// @Getter
    public ContentType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public MultipartFile getImage() {
        return image;
    }
}
