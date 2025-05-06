package com.blog.mypage.service;

import com.blog.mypage.mapper.MyPageMapper;
import com.blog.mypage.record.MyInfoRecord;
import com.blog.mypage.record.MyInfoUpdateRecord;
import com.blog.mypage.record.PasswordChangeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MyPageService {

    @Autowired
    private MyPageMapper mapper;

    public MyInfoRecord getMyInfo(String memberId){
        return mapper.selectMyInfo(memberId);
    }

    public void updateMyInfo(String memberId, MyInfoUpdateRecord update) {
        mapper.updateMyInfo(
                memberId,
                update.name(),
                update.nickname(),
                update.intro(),
                update.birthday()
        );
    }

    public String uploadProfileImg(String memberId, MultipartFile file) throws IOException {
        // 1) 파일 저장
        String folder = "C:/dev/leetsWorkspace/Blog-BE-ItoR/uploads";
        Files.createDirectories(Paths.get(folder));
        String filename = memberId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path target = Paths.get(folder, filename);
        file.transferTo(target);

        String url = "/uploads/" + filename;

        mapper.deleteProfileImg(memberId);
        mapper.insertProfileImg(memberId, url);

        return url;
    }

    public void changePassword(String memberId, PasswordChangeRecord req) {
        // 1) 현재 비밀번호 검증
        String current = mapper.selectPassword(memberId);
        if (!current.equals(req.oldPwd())) {
            throw new IllegalArgumentException("틀린 비밀번호입니다. 다시 입력하세요.");
        }
        // 2) 새 비밀번호 확인
        if (!req.newPwd().equals(req.newPwdConfirm())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }
        // 3) 업데이트
        mapper.updatePassword(memberId, req.newPwd());
    }
















}//class
