package com.blog.domain.post.repository;

import com.blog.domain.post.domain.ContentType;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.domain.PostContent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostContentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostContentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 글 내용 저장
    public void save(PostContent content) {
        String sql = "INSERT INTO post_content (post_id, type, content, sequence) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                content.getPostId(),
                content.getType().name(),  // Enum → 문자열
                content.getContent(),
                content.getSequence()
        );
    }

    // 내용 list 조회하기
    public List<PostContent> findByPostIdOrderBySequence(Long postId) {
        String sql = "SELECT * FROM post_content WHERE post_id = ? ORDER BY sequence";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            ContentType type = ContentType.valueOf(rs.getString("type"));
            String content = rs.getString("content");
            int sequence = rs.getInt("sequence");
            return new PostContent(id, postId, type, content, sequence);
        }, postId);
    }


    public void update(List<PostContent> postContents) {
        String sql = "UPDATE post_content SET type = ?, content = ? WHERE post_id = ? AND sequence = ?";

        for (PostContent content : postContents) {
            jdbcTemplate.update(
                    sql,
                    content.getType().toString(),
                    content.getContent(),
                    content.getPostId(),
                    content.getSequence()
            );
        }
    }

    public void deleteByPostId(Long postId) {
        String sql = "DELETE FROM post_content WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

}
