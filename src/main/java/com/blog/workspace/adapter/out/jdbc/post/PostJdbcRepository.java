package com.blog.workspace.adapter.out.jdbc.post;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
