package com.blog.comment.dao;

import com.blog.comment.exception.CommentException;
import com.blog.comment.mapper.CommentMapper;
import com.blog.comment.record.CommentCreateRecord;
import com.blog.comment.record.CommentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDao {

    @Autowired
    private CommentMapper mapper;

    /**
     * 댓글 작성자 ID 조회
     */
    public String getWriterId(Integer commentNum) {
        return mapper.selectWriterId(commentNum);
    }

    /**
     * 특정 게시글의 댓글 목록을 조회한다.
     *
     * @param postId 게시글 번호
     * @return 댓글 리스트
     */
    public List<CommentRecord> getComments(int postId) {
        return mapper.findByPostId(postId);
    }


    /**
     * 댓글을 DB에 저장한다.
     *
     * @param record 저장할 CommentRecord
     * @return 삽입된 행 수
     */
    public int insertComment(CommentRecord record) {
        // record → 생성 전용 record 로 변환
        CommentCreateRecord param = new CommentCreateRecord(
                record.memberId(),
                record.postId(),
                record.content()
        );
        return mapper.insertComment(param);
    }

    /**
     * 댓글을 수정한다.
     *
     * @param record 수정할 CommentRecord
     */
    public void updateComment(CommentRecord record) {
        if (record.content() == null || record.content().isBlank()) {
            throw new CommentException("댓글 내용이 비어 있습니다.");
        }
        mapper.update(record);
    }

    /**
     * 댓글을 소프트 삭제 처리한다.
     *
     * @param commentNum 삭제할 댓글 번호
     */
    public void deleteComment(Integer commentNum) {
        mapper.delete(commentNum);
    }















}//class
