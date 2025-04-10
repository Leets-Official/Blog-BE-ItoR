package com.blog.post.vo;

import java.util.List;

public class PostWriteVo {

    private Long postNum;
    private String title;                     // 게시글 제목
    private List<ContentBlockVo> blocks;      // 본문 블록
    private String authorId;

    public PostWriteVo() {}

    @Override
    public String toString() {
        return "PostWriteVo{" +
                "title='" + title + '\'' +
                ", blocks=" + blocks +
                ", postNum=" + postNum +
                ", authorId='" + authorId + '\'' +
                '}';
    }


    public PostWriteVo(String title,
                       List<ContentBlockVo> blocks,
                       Long postNum,
                       String authorId) {
        this.title = title;
        this.blocks = blocks;
        this.postNum = postNum;
        this.authorId = authorId;
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

    public Long getPostNum() {
        return postNum;
    }

    public void setPostNum(Long postNum) {
        this.postNum = postNum;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }






}//class
