package com.blog.workspace.adapter.out.external.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blog.workspace.application.out.image.ImagePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Component
public class S3ImagePersistenceAdapter implements ImagePort {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3ImagePersistenceAdapter(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 이미지 하나 저장
    @Override
    public String uploadFiles(MultipartFile file) throws IOException {

        /// S3 관련 내용 저장
        String filename = createFileName(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(new PutObjectRequest(bucket, filename, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        return amazonS3.getUrl(bucket, filename).toString();
    }

    // 이미지 하나 삭제
    @Override
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucket, fileName);
    }

    /// 내부 함수
    // 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    private String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(fileName);
    }

}
