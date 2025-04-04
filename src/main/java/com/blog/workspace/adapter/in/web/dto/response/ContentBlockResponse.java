package com.blog.workspace.adapter.in.web.dto.response;

import com.blog.workspace.domain.post.ContentBlock;

import java.util.List;

public class ContentBlockResponse {

    private String content;

    private int ord;

    // 생성자
    public ContentBlockResponse(String content, int ord) {
        this.content = content;
        this.ord = ord;
    }

    /// 정적 팩토리 메서드
    public static ContentBlockResponse from(ContentBlock contentBlock) {
        return new ContentBlockResponse(contentBlock.getContent(), contentBlock.getOrd());
    }

    public static List<ContentBlockResponse> from(List<ContentBlock> contentBlocks) {
        return contentBlocks.stream()
                .map(ContentBlockResponse::from)
                .toList();
    }

    /// @Getter
    public String getContent() {
        return content;
    }

    public int getOrd() {
        return ord;
    }
}
