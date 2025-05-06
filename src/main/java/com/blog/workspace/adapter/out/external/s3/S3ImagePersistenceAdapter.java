package com.blog.workspace.adapter.out.external.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blog.workspace.application.out.image.ImagePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<String> uploadFiles(List<MultipartFile> files){

        List<String> result = new ArrayList<>();

        // forEach 구문을 통해 files 리스트로 넘어온 파일들을 순차적으로 result 에 추가
        files.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()){
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e){
                throw new
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            result.add(fileName);

        });

        return result;
    }

    @Override
    public ResponseEntity<Resource> getFile(String fileName) {

        String urlStr = amazonS3.getUrl(bucket, fileName).toString();
        Resource resource;
        HttpHeaders headers = new HttpHeaders();
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            resource = new InputStreamResource(inputStream);

            // MIME 타입 설정
            String mimeType = urlConnection.getContentType();
            if (mimeType == null) {
                Path path = Paths.get(fileName);
                mimeType = Files.probeContentType(path);
            }
            headers.add("Content-Type", mimeType);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    // 이미지 하나 삭제
    @Override
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucket, fileName);
    }

    @Override
    public void deleteFiles(List<String> fileName) {
        fileName.forEach(this::deleteFile);
    }

    /// 내부 함수
    // 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    private String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(fileName);
    }

}
