package com.blog.domain.comments.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Comments {

    private int id;
    private int post_id;
    private int user_id;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Comments(int id, int post_id, int user_id, String content, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    // 정적 메소드
    public static Comments of(int id, int post_id, int user_id, String content, LocalDateTime created_at, LocalDateTime updated_at){
        return new Comments(id, post_id, user_id, content, created_at, updated_at);
    }

    public int getCommentsId(){
        return id;
    }

    public int getPostId(){
        return post_id;
    }

    public int getUserId(){
        return user_id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt(){
        return created_at;
    }

    public LocalDateTime getUpdatedAt(){
        return updated_at;
    }

    public void changedPostId(int post_id){
        this.post_id =  post_id;
    }

    public void changedUserId(int user_id){
        this.user_id = user_id;
    }

    public void changedContent(String content){
        this.content = content;
    }


}
