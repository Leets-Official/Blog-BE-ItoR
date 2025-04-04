package com.blog.workspace.adapter.in.web.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PostListResponse {

    /*
       [제목, 게시글 내용, 게시글 썸네일 이미지, 게시글 작성 일시,작성자, 작성자 프로필 이미지, 댓글 개수]가 보여야 한다.
       - 게시글 작성 일시 표기, 작성 후, 24시간 이상 [ MM DD, YYYY]
       - 게시글 작성 일시 표기, 작성 후, 23시간 이전 [ HH 이전]
     */


    private Long id;

    private String title;

    private List<ContentBlockResponse> content;

    private String thumbnail;

    // 표기법 준수 필요!
    private LocalDateTime createdAt;

    private String author;

    private String authorThumbnail;

    private Integer commentCount;

}
