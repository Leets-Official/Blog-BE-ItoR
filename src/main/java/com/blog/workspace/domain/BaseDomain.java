package com.blog.workspace.domain;

import java.time.LocalDateTime;

public abstract class BaseDomain {

    private LocalDateTime created;
    private LocalDateTime updated;

    public BaseDomain(LocalDateTime created, LocalDateTime updated) {
        this.created = created;
        this.updated = updated;
    }
}
