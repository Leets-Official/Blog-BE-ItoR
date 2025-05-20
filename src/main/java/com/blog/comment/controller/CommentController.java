package com.blog.comment.controller;

import com.blog.comment.exception.CommentException;
import com.blog.comment.record.CommentDeleteRecord;
import com.blog.comment.record.CommentRecord;
import com.blog.comment.service.CommentService;
import com.blog.member.vo.MemberVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    // 댓글 목록 조회
    @GetMapping("/showComment")
    public ResponseEntity<List<CommentRecord>> showComments(
            @RequestParam Integer postId
    ) {
        // postId가 null이면 명시적인 예외 처리
        if (postId == null) {
            throw new IllegalArgumentException("postId 파라미터는 필수입니다.");
        }
        return ResponseEntity.ok(service.getComments(postId));
    }

    //댓글 작성 (삽입) : 로그인 시에만 가능하다.
    @PostMapping("/write")
    public ResponseEntity<Integer> writeComment(
            @RequestBody CommentRecord rec,
            HttpSession session
    ) {
        MemberVo loginMember = (MemberVo) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new CommentException("로그인이 필요합니다.");
        }

        CommentRecord toInsert = new CommentRecord(
                rec.commentNum(),
                loginMember.getId(),
                rec.postId(),
                rec.content(),
                null,
                null,
                false
        );
        int newId = service.createComment(toInsert);
        return ResponseEntity.ok(newId);
    }

    // 댓글 수정 : 작성자만
    @PutMapping("/edit")
    public ResponseEntity<?> editComment(
            @RequestBody CommentRecord rec,
            HttpSession session
    ) {
        MemberVo loginMember = (MemberVo) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new CommentException("로그인이 필요합니다.");
        }
        CommentRecord toUpdate = new CommentRecord(
                rec.commentNum(),
                loginMember.getId(),
                rec.postId(),
                rec.content(),
                rec.createdAt(),
                rec.updateAt(),
                rec.isDeleted()
        );
        service.updateComment(toUpdate);
        return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/{commentNum}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Integer commentNum,
            HttpSession session
    ) {
        MemberVo loginMember = (MemberVo) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new CommentException("로그인이 필요합니다.");
        }
        CommentDeleteRecord toDelete = new CommentDeleteRecord(
                commentNum,
                loginMember.getId(),
                true    // isDeleted
        );
        service.deleteComment(toDelete);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

















}//class
