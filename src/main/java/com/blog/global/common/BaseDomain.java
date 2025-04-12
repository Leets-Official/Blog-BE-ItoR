package com.blog.global.common;

import java.time.LocalDateTime;

public abstract class BaseDomain {

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public BaseDomain(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BaseDomain(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BaseDomain() {}

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}

