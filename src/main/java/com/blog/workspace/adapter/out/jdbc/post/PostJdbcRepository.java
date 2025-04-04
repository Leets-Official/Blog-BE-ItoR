package com.blog.workspace.adapter.out.jdbc.post;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class PostJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /// GPT 참조 ..
    public PostJdbc save(PostJdbc postJdbc) {
        String sql = "INSERT INTO post (user_id, title, created_at, updated_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, postJdbc.getUserId());
            ps.setString(2, postJdbc.getTitle());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(postJdbc.getCreatedAt()));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(postJdbc.getUpdatedAt()));
            return ps;
        }, keyHolder);

        // ** 자동 생성된 ID 가져오기
        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            postJdbc.setId(generatedId.longValue());
        }

        return postJdbc;
    }

    public Optional<PostJdbc> findById(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        return jdbcTemplate.query(sql, postRowMapper(), id)
                .stream()
                .findFirst();
    }

    private RowMapper<PostJdbc> postRowMapper() {
        return (rs, rowNum) -> {
            return new PostJdbc(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("title"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );
        };
    }

}
