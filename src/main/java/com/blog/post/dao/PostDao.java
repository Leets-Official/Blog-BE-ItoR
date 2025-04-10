package com.blog.post.dao;

import com.blog.post.exception.PostException;
import com.blog.post.mapper.PostMapper;
import com.blog.post.record.PostRecord;
import com.blog.post.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDao {

    @Autowired
    private PostMapper mapper;


    /**
     * 게시글 목록을 페이징 조회한다.
     *
     * @param page  페이지 번호 (0부터 시작)
     * @param size  한 페이지당 게시글 수
     * @return 게시글 리스트
     */

    public List<PostRecord> getPostList(int page, int size) {
        int offset = page * size;
        return mapper.selectPostList(offset, size);
    }

    /**
     * 게시글 상세 조회
     * @param postNum 게시글 번호
     * @return PostDetailVo 객체 (본문 포함)
     */
    public PostDetailVo getPostDetail(int postNum) {
        return mapper.selectPostDetail(postNum);
    }



    /**
     * 게시글과 콘텐츠 블록을 DB에 저장
     * PostWriteVo 작성용 VO (제목 + 블록 리스트 + authorId)
     * @return 생성된 게시글 번호 (post_num)
     */
    public Long insertPost(PostWriteVo vo) {

        mapper.insertPost(vo);
        Long postNum = vo.getPostNum();

        List<ContentBlockVo> blocks = vo.getBlocks();
        if (blocks != null && !blocks.isEmpty()) {
            mapper.insertContentBlock(postNum, blocks);
        }

        return postNum;
    }


    /*
    작성자 아이디 찾기
     */
    public String getAuthorId(Long postNum) {return mapper.selectAuthorId(postNum);}

    /**
     * 게시글 수정
     * 본인 확인 → 제목 수정 → 블록 삭제 & 새로 저장
     */
    public void updatePost(PostEditVo vo) {

        mapper.updatePostTitle(vo.getPostNum(), vo.getTitle());
        mapper.deleteBlocks(vo.getPostNum());

        List<ContentBlockVo> blocks = vo.getBlocks();

        if (blocks == null || blocks.isEmpty()) {
            throw new PostException("게시글 내용이 비어 있습니다.");
        }
            mapper.insertContentBlock(vo.getPostNum(), blocks);
    }

    /**
     * 게시글 삭제 (소프트 딜리트)
     * 로그인한 사용자가 작성자인 경우 삭제 처리
     */
    public void deletePost(Long postId, String authorId) { mapper.deletePost(postId);}



}//class
