package com.blog.workspace.adapter.out.jdbc;

import java.time.LocalDateTime;

public abstract class BaseJdbc {

    private LocalDateTime created;
    private LocalDateTime updated;

    public BaseJdbc(LocalDateTime created, LocalDateTime updated) {
        this.created = created;
        this.updated = updated;
    }
}
