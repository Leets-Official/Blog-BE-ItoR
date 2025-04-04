package com.blog.workspace.adapter.out.jdbc.post;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

import java.util.List;

@Repository
public class PostJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /// GPT의 참조를 ...
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

        // !** 자동 생성된 ID 가져오기 **!
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

    /// GPT의 참조를 ...
    public Page<PostJdbc> findAllByUserId(Pageable pageable, Long userId) {
        String sql = "SELECT * FROM post WHERE user_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        String countSql = "SELECT COUNT(*) FROM post WHERE user_id = ?";

        // 특정 userId의 전체 게시글 수 조회
        Integer totalElements = jdbcTemplate.queryForObject(countSql, Integer.class, userId);
        if (totalElements == null) totalElements = 0;

        // 게시글 목록 조회
        List<PostJdbc> posts = jdbcTemplate.query(
                sql,
                postRowMapper(),
                userId,
                pageable.getSize(),
                pageable.getOffset()
        );

        return new Page<>(posts, pageable, totalElements);
    }

    // 유저가 해당 게시글의 작성자인지 확인
    public boolean existsByUserIdAndPostId(Long userId, Long id) {
        String sql = "SELECT COUNT(*) FROM post WHERE id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id, userId);
        return count != null && count > 0;
    }

    // 수정
    public PostJdbc update(PostJdbc postJdbc) {
        String sql = "UPDATE post SET title = ?, updated_at = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                postJdbc.getTitle(),
                java.sql.Timestamp.valueOf(LocalDateTime.now()),
                postJdbc.getId());

        return postJdbc;
    }


    // 게시글 삭제
    public void deleteById(Long id) {
        String sql = "DELETE FROM post WHERE id = ?";
        jdbcTemplate.update(sql, id);
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
