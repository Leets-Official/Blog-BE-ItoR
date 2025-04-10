package com.blog.post.vo;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailVo {

    private Long id;
    private String title;
    private String writerName;
    private String writerProfileImg;
    private LocalDateTime createdAt;
    private List<ContentBlockVo> blocks;

    public PostDetailVo() {}

    public PostDetailVo(Long id,
                        String title,
                        String writerName,
                        String writerProfileImg,
                        LocalDateTime createdAt,
                        List<ContentBlockVo> blocks) {
        this.id = id;
        this.title = title;
        this.writerName = writerName;
        this.writerProfileImg = writerProfileImg;
        this.createdAt = createdAt;
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "PostDetailVo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", writerName='" + writerName + '\'' +
                ", writerProfileImg='" + writerProfileImg + '\'' +
                ", createdAt=" + createdAt +
                ", blocks=" + blocks +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getWriterProfileImg() {
        return writerProfileImg;
    }

    public void setWriterProfileImg(String writerProfileImg) {
        this.writerProfileImg = writerProfileImg;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ContentBlockVo> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<ContentBlockVo> blocks) {
        this.blocks = blocks;
    }
}//class
