package com.blog.workspace.domain.post;

public class ContentBlock {

    private Long id;

    private Long postId;

    private ContentType type;

    // 이미지 주소 혹은 글 내용이 포함된다.
    private String content;

    private int ord;

    /// 생성자
    public ContentBlock(Long id, Long postId,ContentType type, String content, int ord) {
        this.id = id;
        this.postId = postId;
        this.type = type;
        this.content = content;
        this.ord = ord;
    }

    /// 정적 팩토리 메서드 사용 -> 서비스 계층에서 사용
    public static ContentBlock of(Long postId, ContentType type, String content, int ord) {
        return new ContentBlock(null, postId, type, content, ord);
    }

    /// 정적 팩토리 메서드 사용 -> adapter.out 에서 사용
    public static ContentBlock fromDB(Long id, Long postId,ContentType type, String content, int ord) {
        return new ContentBlock(id, postId, type, content, ord);
    }

    /// 비즈니스 로직


    /// @Getter
    public Long getId() {
        return id;
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

    public Long getPostId() {
        return postId;
    }


}
