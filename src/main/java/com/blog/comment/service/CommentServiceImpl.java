package com.blog.comment.service;

import com.blog.comment.dao.CommentDao;
import com.blog.comment.exception.CommentException;
import com.blog.comment.record.CommentDeleteRecord;
import com.blog.comment.record.CommentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentDao dao;

    @Override
    public List<CommentRecord> getComments(int postId) {
        return dao.getComments(postId);
    }

    @Override
    @Transactional
    public int createComment(CommentRecord record) {
        if (record.content() == null || record.content().isBlank()) {
            throw new CommentException("댓글 내용이 비어 있습니다.");
        }
        return dao.insertComment(record);
    }

    @Override
    @Transactional
    public void updateComment(CommentRecord record) {
        // 1) DB에서 실제 작성자 조회
        String writer = dao.getWriterId(record.commentNum());
        // 2) 헤더로 넘어온 record.memberId() 와 비교
        if (!writer.equals(record.memberId())) {
            throw new CommentException("작성자만 수정할 수 있습니다.");
        }
        // 3) content 는 이미 record 에 담겨 있으니 바로 호출
        dao.updateComment(record);
    }

//    @Override
//    @Transactional
//    public void deleteComment(CommentRecord record) {
//        String writer = dao.getWriterId(record.commentNum());
//        if (!writer.equals(record.memberId())) {
//            throw new CommentException("작성자만 삭제할 수 있습니다.");
//        }
//        dao.deleteComment(record.commentNum());
//    }

    @Override
    @Transactional
    public void deleteComment(CommentDeleteRecord record) {
        int no = record.commentNum();
        // 1) 작성자 조회
        String writer = dao.getWriterId(no);
        // 2) 레코드 자체가 없거나, 매퍼 누락 시 대비
        if (writer == null) {
            throw new CommentException("존재하지 않는 댓글입니다.");
        }
        // 3) 본인 검증
        if (!writer.equals(record.memberId())) {
            throw new CommentException("작성자만 삭제할 수 있습니다.");
        }
        // 4) soft delete 수행
        dao.deleteComment(no);
    }












}//class
