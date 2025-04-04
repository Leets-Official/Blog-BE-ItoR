package com.blog.workspace.adapter.out.jdbc.post;

import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.ContentType;
public class ContentBlockJdbc {

    private Long id;

    private Long postId;

    private ContentType type;

    // 이미지 주소 혹은 글 내용이 포함된다.
    private String content;

    private int ord;

    /// 생성자
    public ContentBlockJdbc(Long id, Long postId,ContentType type, String content, int ord) {
        this.id = id;
        this.postId = postId;
        this.type = type;
        this.content = content;
        this.ord = ord;
    }

    // from
    public static ContentBlockJdbc from(ContentBlock contentBlock) {
        return new ContentBlockJdbc(null, contentBlock.getPostId(), contentBlock.getType(), contentBlock.getContent(), contentBlock.getOrd());
    }

    // toDomain
    public ContentBlock toDomain(){
        return ContentBlock.fromDB(id, postId, type, content, ord);
    }


    /// @Getter
    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public ContentType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getOrd() {
        return ord;
    }
}
