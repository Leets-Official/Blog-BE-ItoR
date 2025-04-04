package com.blog.workspace.adapter.in.web.dto.response;

import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.Post;
import com.blog.workspace.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailResponse {

    /*
       [제목, 게시글 내용, 게시글 모든 이미지, 게시글 작성 일시,작성자, 작성자 프로필 이미지, 댓글 개수]가 보여야 한다.
     */

    private Long id;

    private String title;

    private List<ContentBlockResponse> content;

    private String thumbnail;

    private UserPostResponse user;

    private Integer commentCount;

    private LocalDateTime createdAt;


    // 생성자
    public PostDetailResponse(Long id, String title, String thumbnail, LocalDateTime createdAt, Integer commentCount, UserPostResponse user,List<ContentBlockResponse> content) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.user = user;
        this.commentCount = commentCount;
    }

    /// 정적 팩토리 메서드
    public static PostDetailResponse from(Post post, User user, List<ContentBlock> content) {

        // 유저 관련 정보
        UserPostResponse userResponse = UserPostResponse.from(user);

        // 블록 관련 정보
        List<ContentBlockResponse> contentResponse = ContentBlockResponse.from(content);

        return new PostDetailResponse(post.getId(), post.getTitle(), "썸네일", post.getCreated(), 0, userResponse, contentResponse);
    }

    ///  @Getter
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<ContentBlockResponse> getContent() {
        return content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public UserPostResponse getUser() {
        return user;
    }
}
