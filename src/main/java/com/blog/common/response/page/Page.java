package com.blog.common.response.page;

import java.util.List;

public class Page<T> {

    private final List<T> content;
    private final int totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int pageSize;

    public Page(List<T> content, Pageable pageable, int totalElements) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageSize = pageable.getSize();
        this.currentPage = pageable.getPage();
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }

    public List<T> getContent() {
        return content;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
