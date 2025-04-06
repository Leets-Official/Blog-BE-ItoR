package com.blog.global.common;

import java.time.LocalDate;

public abstract class BaseDomain {

    protected LocalDate createdAt;
    protected LocalDate updatedAt;

    public BaseDomain(LocalDate createdAt, LocalDate updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BaseDomain(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public BaseDomain() {}

    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

}

