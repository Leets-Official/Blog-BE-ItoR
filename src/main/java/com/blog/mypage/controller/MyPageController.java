package com.blog.mypage.controller;

import com.blog.member.vo.MemberVo;
import com.blog.mypage.record.MyInfoRecord;
import com.blog.mypage.record.MyInfoUpdateRecord;
import com.blog.mypage.record.PasswordChangeRecord;
import com.blog.mypage.record.ProfileImgUploadRecord;
import com.blog.mypage.service.MyPageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.awt.SystemColor.info;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MyPageService service;

    //내 정보 조회하기
    @GetMapping("/show")
    public ResponseEntity<MyInfoRecord> getMyInfo(HttpSession session){
        MemberVo loginMember = (MemberVo) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        String memberId = loginMember.getId();

        MyInfoRecord info =service.getMyInfo(memberId);
        return ResponseEntity.ok(info);
    }
    
    //내 정보 수정하기
    @PutMapping("/edit")
    public ResponseEntity<Void> updateMyInfo(
            @RequestBody MyInfoUpdateRecord update,
            HttpSession session
    ) {
        var loginMember = (com.blog.member.vo.MemberVo)
                session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }

        service.updateMyInfo(loginMember.getId(), update);
        return ResponseEntity.noContent().build();
    }

    //프로필 사진 삽입 하기
    @PostMapping(
            value = "/info/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProfileImgUploadRecord> uploadPhoto(
            @RequestPart("file") MultipartFile file,
            HttpSession session
    ) throws IOException {
        MemberVo login = (MemberVo) session.getAttribute("loginMember");
        if (login == null) {
            return ResponseEntity.status(401).build();
        }
        String url = service.uploadProfileImg(login.getId(), file);
        return ResponseEntity.ok(new ProfileImgUploadRecord(url));
    }


    //비밀번호 수정하기
    @PutMapping("/info/password")
    public ResponseEntity<String> changePassword(
            @RequestBody PasswordChangeRecord req,
            HttpSession session
    ) {
        MemberVo login = (MemberVo) session.getAttribute("loginMember");
        if (login == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            service.changePassword(login.getId(), req);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }







}//class
