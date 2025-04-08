package com.blog.domain.posts.domain;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PostBlocks {
    private int id;
    private int post_id;
    private String content;
    private String image_url;

    // 생성자
    public PostBlocks(int post_id, String content, String image_url){
        this.post_id = post_id;
        this.content = content;
        this.image_url = image_url;
    }


    // 정적 메소드
    public static PostBlocks createPostBlock(int post_id, String content, String image_url){
        return new PostBlocks(post_id, content, image_url);
    }

    public static PostBlocks fromResultSet(ResultSet rs) {
        try {
            return new PostBlocks(
                    rs.getInt("post_id"),
                    rs.getString("content"),
                    rs.getString("image_url")
            );
        } catch (SQLException e){

            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }

    // Getter & Setter
    public void changePostId(int post_id) {
        this.post_id = post_id;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public int getPostBlocksId() {
        return id;
    }

    public int getPostId() {
        return post_id;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return image_url;
    }
}
