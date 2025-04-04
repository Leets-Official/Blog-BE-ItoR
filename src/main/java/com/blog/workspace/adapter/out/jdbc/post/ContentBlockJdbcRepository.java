package com.blog.workspace.adapter.out.jdbc.post;

import com.blog.workspace.domain.post.ContentType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContentBlockJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ContentBlockJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ContentBlockJdbc save(ContentBlockJdbc contentBlock) {
        String sql = "INSERT INTO content_block (post_id, type, content, ord) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                contentBlock.getPostId(),
                contentBlock.getType().name(),
                contentBlock.getContent(),
                contentBlock.getOrd()
        );
        return contentBlock;
    }


    public List<ContentBlockJdbc> findByPostId(Long postId) {
        String sql = "SELECT * FROM content_block WHERE post_id = ? ORDER BY ord ASC";

        return jdbcTemplate.query(sql, contentBlockRowMapper(), postId);
    }

    private RowMapper<ContentBlockJdbc> contentBlockRowMapper() {
        return (rs, rowNum) -> new ContentBlockJdbc(
                rs.getLong("id"),
                rs.getLong("post_id"),
                ContentType.valueOf(rs.getString("type")),
                rs.getString("content"),
                rs.getInt("ord")
        );
    }
}
