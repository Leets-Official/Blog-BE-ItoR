package com.blog.post.record;

public record PostRecord<LocalDateTime>(
        Long id,                    // 게시글 ID
        String title,               // 제목
        String previewContent,      // 내용 요약 (SUBSTRING 처리 등)
        String thumbnailUrl,        // 썸네일 이미지 URL
        String writerName,          // 작성자 닉네임
        String writerProfileImg,    // 작성자 프로필 이미지 URL
        LocalDateTime createdAt,    // 작성일시
        int commentCount            // 댓글 수
)

{ }//record
