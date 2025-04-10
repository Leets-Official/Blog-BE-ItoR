package com.blog.post.vo;

public class ContentBlockVo {

    private String type;
    private String content;
    private String postImgUrl;
    private int blockOrder;

    public ContentBlockVo() {}


    public ContentBlockVo(String type,
                          String content,
                          String postImgUrl,
                          int blockOrder) {
        this.type = type;
        this.content = content;
        this.postImgUrl = postImgUrl;
        this.blockOrder = blockOrder;
    }

    @Override
    public String toString() {
        return "ContentBlockVo{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", postImgUrl='" + postImgUrl + '\'' +
                ", blockOrder=" + blockOrder +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public int getBlockOrder() {
        return blockOrder;
    }

    public void setBlockOrder(int blockOrder) {
        this.blockOrder = blockOrder;
    }





}//class
