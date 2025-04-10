package com.blog.post.service;

import com.blog.post.record.PostRecord;
import com.blog.post.vo.PostDetailVo;
import com.blog.post.vo.PostEditVo;
import com.blog.post.vo.PostWriteVo;

import java.util.List;

public interface PostService {


    /**
     * 게시글 목록 조회 (페이징)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지당 게시글 수
     * @return 게시글 목록
     */
    List<PostRecord> getPostList(int page, int size);

    /**
     * 게시글 상세 조회
     * @param postNum 게시글 번호
     * @return 상세 정보 (본문 포함)
     */
    PostDetailVo getPostDetail(int postNum);

    /**
     * 게시글 작성
     * PostWriteVo 작성용 VO (제목, 블록, 작성자 ID 포함)
     * @return 생성된 게시글 번호
     */
    Long createPost(PostWriteVo vo);

    /**
     * 게시글 수정
     * 본인 게시글만 수정 가능
     */
    void editPost(PostEditVo vo);

    /**
     * 게시글 삭제 (로그인 후 작성자만 삭제 가능)
     */
    void deletePost(Long postId, String authorId);









}//interface
