package com.blog.post.domain;

public class PostContent {

    private int contentId;
    private int postId;
    private int userId;
    private PostContentType contentType;
    private String content;
    private int order;

    public PostContent(
            int contentId,
            int postId,
            int userId,
            PostContentType contentType,
            String content,
            int order
    ) {
        this.contentId = contentId;
        this.postId = postId;
        this.userId = userId;
        this.contentType = contentType;
        this.content = content;
        this.order = order;
    }

    public int getContentId() { return contentId; }
    public int getPostId() { return postId; }
    public int getUserId() { return userId; }
    public PostContentType getContentType() { return contentType; }
    public String getContent() { return content; }
    public int getOrder() { return order; }

    public void setContent(String content) { this.content = content; }
    public void setOrder(int order) { this.order = order; }
}
