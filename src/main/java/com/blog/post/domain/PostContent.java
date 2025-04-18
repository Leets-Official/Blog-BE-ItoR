package com.blog.post.domain;

public class PostContent {

    private Integer contentId;
    private Integer postId;
    private Integer userId;
    private PostContentType contentType;
    private String content;
    private Integer order;

    public PostContent(
            Integer contentId,
            Integer postId,
            Integer userId,
            PostContentType contentType,
            String content,
            Integer order
    ) {
        this.contentId = contentId;
        this.postId = postId;
        this.userId = userId;
        this.contentType = contentType;
        this.content = content;
        this.order = order;
    }

    public Integer getContentId() { return contentId; }
    public Integer getPostId() { return postId; }
    public Integer getUserId() { return userId; }
    public PostContentType getContentType() { return contentType; }
    public String getContent() { return content; }
    public Integer getOrder() { return order; }

    public void setContent(String content) { this.content = content; }
    public void setOrder(Integer order) { this.order = order; }
}
