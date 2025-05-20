package com.blog.domain.image.service;

import com.blog.domain.image.domain.Image;
import com.blog.domain.image.dto.ImageResponse;
import com.blog.domain.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageService {
  private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }
  public ImageResponse upload(MultipartFile file) {
    try {
      String originalName = file.getOriginalFilename();
      String ext = originalName.substring(originalName.lastIndexOf("."));
      String storedName = UUID.randomUUID() + ext;
      Path storedPath = Path.of(UPLOAD_DIR + storedName);

      Files.createDirectories(storedPath.getParent()); // 디렉토리 없으면 생성
      file.transferTo(storedPath.toFile());

      String url = "/images/" + storedName;

      Image image = new Image(
          UUID.randomUUID(),
          originalName,
          storedName,
          url,
          LocalDateTime.now()
      );

      imageRepository.save(image);
      return ImageResponse.from(image);
    } catch (Exception e) {
      throw new RuntimeException("이미지 저장 실패", e);
    }
  }
}
