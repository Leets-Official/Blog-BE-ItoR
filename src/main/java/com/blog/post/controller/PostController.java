package com.blog.post.controller;

import com.blog.member.vo.MemberVo;
import com.blog.post.record.PostRecord;
import com.blog.post.service.PostService;
import com.blog.post.vo.ContentBlockVo;
import com.blog.post.vo.PostDetailVo;
import com.blog.post.vo.PostEditVo;
import com.blog.post.vo.PostWriteVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
                                           HttpSession session) {
        MemberVo member =(MemberVo) session.getAttribute("loginMember");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        vo.setAuthorId(member.getId());
        long postNum = service.createPost(vo);

        return ResponseEntity.ok(postNum);
    }

    //게시글 수정하기
    @PutMapping("/edit")
    public ResponseEntity<?> editPost(@RequestBody PostEditVo vo,
                                      HttpSession session) {
        MemberVo member =(MemberVo) session.getAttribute("loginMember");
        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        //--------------------블록 검증--------------------------
        if (vo.getPostNum() == null
                || vo.getTitle() == null || vo.getTitle().isBlank()
                || vo.getBlocks() == null || vo.getBlocks().isEmpty()
        ) {
            return ResponseEntity
                    .badRequest()
                    .body("postNum, 제목, 내용을 모두 입력해주세요.");
        }
        // text 블록만 내용 검증
        for (ContentBlockVo b : vo.getBlocks()) {
            if ("text".equals(b.getType())
                    && (b.getContent() == null || b.getContent().isBlank())
            ) {
                return ResponseEntity
                        .badRequest()
                        .body("텍스트 블록에 내용이 필요합니다.");
            }
        }

        vo.setAuthorId(member.getId());
        service.editPost(vo);
        return ResponseEntity.ok("수정 완료");
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        HttpSession session) {
        MemberVo member =(MemberVo) session.getAttribute("loginMember");
        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }
        service.deletePost(postId,member.getId());
        return ResponseEntity.ok("게시글 삭제 완료");
    }




}//class
