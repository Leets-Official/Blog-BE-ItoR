package com.blog.common.response.page;

public class Pageable {

    private final int page;
    private final int size;

    // 생성자
    public Pageable(int page, int size) {
        this.page = page;
        this.size = size;
    }

    ///  정적 스태틱 메서드
    public static Pageable of(int page, int size) {
        return new Pageable(page, size);
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return (page - 1) * size;  // OFFSET 계산
    }
}

