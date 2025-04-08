package com.blog.domain.posts.domain.repository;

import com.blog.domain.posts.domain.PostBlocks;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostBlocksRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostBlocksRepository(JdbcTemplate jdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;
    }

    public void addPostBlock(PostBlocks block) {
        String sql = "INSERT INTO post_blocks (post_id, content, image_url) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                block.getPostId(),
                block.getContent(),
                block.getImageUrl());
    }

    public List<PostBlocks> getPostBlockListByPostId(int postId){
        String sql = "SELECT post_id, content, image_url FROM post_blocks WHERE post_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> PostBlocks.fromResultSet(rs), postId);
    }

    public List<PostBlocks> getPostBlockListWithIdByPostId(int postId){
        String sql = "SELECT * FROM post_blocks WHERE post_id = ? ORDER BY id ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> PostBlocks.fromResultSetWithId(rs), postId);
    }

    public void updatePostBlock(int id, PostBlocks postBlock){
        String sql = "UPDATE post_blocks SET content = ?, image_url = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                postBlock.getContent(),
                postBlock.getImageUrl(),
                id);
    }

    public void deletePostBlock(int id){
        String sql = "DELETE FROM post_blocks WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
