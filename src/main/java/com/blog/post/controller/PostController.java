package com.blog.post.controller;

import com.blog.post.record.PostRecord;
import com.blog.post.service.PostService;
import com.blog.post.vo.PostDetailVo;
import com.blog.post.vo.PostEditVo;
import com.blog.post.vo.PostWriteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService service;

    //게시글 목록 조회
    @GetMapping("/showPost")
    public List<PostRecord> showPost(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size){
        return service.getPostList(page,size);
    }

    //게시글 상세 조회
    @GetMapping("/{postNum}")
    public PostDetailVo getPostDetail(@PathVariable int postNum) {
        return service.getPostDetail(postNum);
    }

    //게시글 작성하기
    @PostMapping("/write")
    public ResponseEntity<Long> createPost(@RequestBody PostWriteVo vo,
                                           @RequestHeader ("X-USER-ID") String userId) {

        vo.setAuthorId(userId);
        long postNum = service.createPost(vo);

        return ResponseEntity.ok(postNum);
    }

    //게시글 수정하기
    @PutMapping("/edit")
    public ResponseEntity<?> editPost(@RequestBody PostEditVo vo,
                                      @RequestHeader("X-USER-ID") String userId) {
        vo.setAuthorId(userId);
        service.editPost(vo);
        return ResponseEntity.ok("수정 완료");
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        @RequestHeader("X-USER-ID") String userId) {
        service.deletePost(postId,userId);
        return ResponseEntity.ok("게시글 삭제 완료");
    }




}//class
