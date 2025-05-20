package com.blog.domain.image.controller;

import com.blog.domain.image.dto.ImageResponse;
import com.blog.domain.image.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {
  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @PostMapping("/upload")
  public ResponseEntity<ImageResponse> upload(@RequestParam("file") MultipartFile file) {
    ImageResponse response = imageService.upload(file);
    return ResponseEntity.ok(response);
  }
}
