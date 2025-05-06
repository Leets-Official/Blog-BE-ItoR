package com.blog.comment.controller;

import com.blog.comment.record.CommentRecord;
import com.blog.comment.service.CommentService;
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
            @RequestHeader("X-USER-ID") String userId
    ) {
        CommentRecord toInsert = new CommentRecord(
                rec.commentNum(),
                userId,
                rec.postId(),
                rec.content(),
                null,
                null,
                false
        );
        return ResponseEntity.ok(service.createComment(toInsert));
    }

    // 댓글 수정 : 작성자만
    @PutMapping("/edit")
    public ResponseEntity<?> editComment(
            @RequestBody CommentRecord rec,
            @RequestHeader("X-USER-ID") String userId
    ) {
        CommentRecord toUpdate = new CommentRecord(
                rec.commentNum(),
                userId,
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
            @RequestBody CommentRecord rec,
            @PathVariable Integer commentNum,
            @RequestHeader("X-USER-ID") String userId
    ) {
        CommentRecord toDelete = new CommentRecord(
                commentNum,
                userId,
                rec.postId(),
                null,
                null,
                null,
                false
        );
        service.deleteComment(toDelete);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

















}//class
