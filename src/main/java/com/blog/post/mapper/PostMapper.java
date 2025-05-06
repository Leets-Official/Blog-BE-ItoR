package com.blog.post.mapper;

import com.blog.post.record.PostRecord;
import com.blog.post.vo.ContentBlockVo;
import com.blog.post.vo.PostDetailVo;
import com.blog.post.vo.PostWriteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    //--------조회-----------

    // 게시글 목록 조회 (페이징 처리 포함)
    List<PostRecord> selectPostList(@Param("offset") int offset, @Param("limit") int limit);

    //게시글 상세 조회
    PostDetailVo selectPostDetail(@Param("postNum") int postNum );

    //작성자 ID 조회
    String selectAuthorId(@Param("postId") Long postId);


    //--------등록-----------

    //게시글 등록
    int insertPost(PostWriteVo vo);

    //블록 리스트 등록
    int insertContentBlock(@Param("postId") Long postId, @Param("blocks") List<ContentBlockVo> blocks);




    //--------수정-----------

    //게시글 제목 수정
    void updatePostTitle(@Param("postId") Long postId, @Param("title") String title);

    //기존 블록 단위 삭제
    void deleteBlocks(@Param("postId") Long postId);

    //게시글 삭제 (소프트 딜리트)
    void deletePost(@Param("postId") Long postId);



}//interface
