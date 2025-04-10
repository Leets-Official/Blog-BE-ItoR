package com.blog.post.vo;

import java.util.List;

public class PostEditVo {

    private Long postNum;
    private String title;
    private List<ContentBlockVo> blocks;
    private String authorId;

    public PostEditVo() {}

    @Override
    public String toString() {
        return "PostEditVo{" +
                "postNum=" + postNum +
                ", title='" + title + '\'' +
                ", blocks=" + blocks +
                ", authorId='" + authorId + '\'' +
                '}';
    }

    public PostEditVo(Long postNum,
                      String title,
                      List<ContentBlockVo> blocks,
                      String authorId) {
        this.postNum = postNum;
        this.title = title;
        this.blocks = blocks;
        this.authorId = authorId;
    }


    public Long getPostNum() {
        return postNum;
    }

    public void setPostNum(Long postNum) {
        this.postNum = postNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContentBlockVo> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<ContentBlockVo> blocks) {
        this.blocks = blocks;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }




}//class
