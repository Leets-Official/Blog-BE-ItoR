package com.blog.workspace.adapter.in.web.dto.response;

import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.Post;
import com.blog.workspace.domain.user.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PostDetailResponse {

    /*
       [제목, 게시글 내용, 게시글 썸네일 이미지, 게시글 작성 일시,작성자, 작성자 프로필 이미지, 댓글 개수]가 보여야 한다.
       - 게시글 작성 일시 표기, 작성 후, 24시간 이상 [ MM DD, YYYY]
       - 게시글 작성 일시 표기, 작성 후, 23시간 이전 [ HH 이전]
     */


    private Long id;

    private UserPostResponse user;

    private String title;

    private List<ContentBlockResponse> content;

    private String createdAt;

    private Integer commentCount;

    // 생성자
    private PostDetailResponse(Long id, String title, String createdAt, Integer commentCount, UserPostResponse user, List<ContentBlockResponse> content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    /// 정적 팩토리 메서드
    public static PostDetailResponse from(Post post, User user, List<ContentBlock> content) {

        // 유저 관련 정보
        UserPostResponse userResponse = UserPostResponse.from(user);

        // 블록 관련 정보
        List<ContentBlockResponse> contentResponse = ContentBlockResponse.from(content);

        //  날짜 관련 정보 처리
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd.yyyy.", Locale.ENGLISH);
        String formattedDate = post.getCreated().format(formatter);

        return new PostDetailResponse(post.getId(), post.getTitle(), formattedDate, 0, userResponse, contentResponse);
    }

    /// @Getter

    public Long getId() {
        return id;
    }

    public UserPostResponse getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public List<ContentBlockResponse> getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getCommentCount() {
        return commentCount;
    }
}
