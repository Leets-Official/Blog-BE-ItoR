package com.blog.domain.post.repository;

import com.blog.domain.post.domain.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 글 저장
    public Long save(Post post) {
        String sql = "INSERT INTO post (user_id, title, comment_count, created_at) " +
                "VALUES (?, ?, ?, NOW())";

        // 삽입 후 생성된 키 저장
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, post.getUserId());
            ps.setString(2, post.getTitle());
            ps.setInt(3, post.getCommentCount());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    // postId로 게시물 탐색
    public Optional<Post> findById(Long postId) {
        String sql = "SELECT * FROM post WHERE id = ?";
        List<Post> results = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("title"),
                        rs.getInt("comment_count"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ),
                postId
        );

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }


    // 전체 조회
    public List<Post> findAllByUserId(Long userId) {
        String sql = "SELECT * FROM post WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Post(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("title"),
                    rs.getInt("comment_count"),
                    rs.getTimestamp("created_at")
                            .toLocalDateTime(),
                    rs.getTimestamp("updated_at")
                            .toLocalDateTime()
            );
        }, userId);
    }

    // 글 수정
    public void update(Post post) {
        String sql = "UPDATE post SET title = ?, updated_at = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                post.getTitle(),
                LocalDateTime.now(), // 현재 시간으로 updated_at 설정
                post.getId());
    }

    // 글 삭제
    public void deleteById(Long postId) {
        String sql = "DELETE FROM post WHERE id = ?";
        jdbcTemplate.update(sql, postId);
    }

    // commentCount 증가
    public void incrementCommentCount(Long postId) {
        String sql = "UPDATE post SET comment_count = comment_count + 1 WHERE id = ?";
        jdbcTemplate.update(sql, postId);
    }

    //commentCount 감소
    public void decreaseCommentCount(Long postId, int count) {
        String sql = "UPDATE post SET comment_count = comment_count - ? WHERE id = ?";
        jdbcTemplate.update(sql, count, postId);
    }


}
