package com.blog.workspace.adapter.in.web.dto.response;

import com.blog.common.util.DateFormatUtil;
import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.Post;
import com.blog.workspace.domain.user.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id","user","title","thumbnail","content","commentCount","createdAt"})
public class PostListResponse {

    /*
       [제목, 게시글 내용, 게시글 모든 이미지, 게시글 작성 일시,작성자, 작성자 프로필 이미지, 댓글 개수]가 보여야 한다.
     */

    private Long id;

    private String title;

    private String content;

    private String thumbnail;

    private UserPostResponse user;

    private Integer commentCount;

    private String createdAt;


    // 생성자
    public PostListResponse(Long id, String title, String thumbnail, String createdAt, Integer commentCount, UserPostResponse user, String content) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.user = user;
        this.commentCount = commentCount;
    }

    /// 정적 팩토리 메서드
    public static PostListResponse from(Post post, UserPostResponse user, String content, String thumbnail) {

        // 시간 관련 정보
        DateFormatUtil dataFormatUtil = new DateFormatUtil();
        String date = dataFormatUtil.formatPostDate(post.getCreated());

        if (thumbnail == null) {
            thumbnail = "default.jpeg";
        }

        return new PostListResponse(post.getId(), post.getTitle(), thumbnail, date, 0, user, content);
    }

    /// @Getter
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public UserPostResponse getUser() {
        return user;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
