package com.blog.domain.posts.domain;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Posts {
    private int id;
    private int user_id;
    private String subject;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // 생성자
    public Posts(int user_id, String subject) {
        this.user_id = user_id;
        this.subject = subject;
    }

    public Posts(int id, int user_id, String subject, LocalDateTime created_at, LocalDateTime updated_at) {
        this.user_id = user_id;
        this.subject = subject;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // 정적 메소드
    public static Posts createPost(int userId, String subject) {
        return new Posts(userId, subject);
    }

    public static Posts fromResultSet(ResultSet rs) {
        try{
            return new Posts(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("subject"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );
        } catch (SQLException e){

            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }

    // Getter & Setter
    public void changeUserId(int user_id) {
        this.user_id = user_id;
    }

    public void changeSubject(String subject) {
        this.subject = subject;
    }

    public int getPostId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return updated_at;
    }
}
