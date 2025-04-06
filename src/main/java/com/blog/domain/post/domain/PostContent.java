package com.blog.domain.post.domain;

public class PostContent {

    private Long id;
    private Long postId;
    private ContentType type; // TEXT, IMAGE
    private String content;
    private int sequence; // 순서

    public PostContent(Long id, Long postId, ContentType type, String content, int sequence) {
        this.id = id;
        this.postId = postId;
        this.type = type;
        this.content = content;
        this.sequence = sequence;
    }
    public PostContent(Long postId, ContentType type, String content, int sequence) {
        this.postId = postId;
        this.type = type;
        this.content = content;
        this.sequence = sequence;
    }

    public int getSequence() {
        return sequence;
    }

    public String getContent() {
        return content;
    }

    public ContentType getType() {
        return type;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getId() {
        return id;
    }

    public static PostContent text(Long postId, String text, int sequence) {
        return new PostContent(postId,ContentType.TEXT,text,sequence);
    }

    public static PostContent image(Long postId, String imageUrl, int sequence) {
        return new PostContent(postId, ContentType.IMAGE, imageUrl, sequence);
    }
}

