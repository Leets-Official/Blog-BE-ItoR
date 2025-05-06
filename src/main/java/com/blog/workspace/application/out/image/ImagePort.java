package com.blog.workspace.application.out.image;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImagePort {

    /*
        이미지 관련 포트는 그대로 사용하도록 한다.
        연관관계없이, 서비스 계층에서 저장된 이미지
        다른 엔티티간의 연관관계를 사용하도록 한다.
     */

    /// SavePort
    // 이미지 하나 저장
    String uploadFiles(MultipartFile multipartFile) throws IOException;

    /// DeletePort
    // 이미지 하나 삭제
    void deleteFile(String fileName);

}
