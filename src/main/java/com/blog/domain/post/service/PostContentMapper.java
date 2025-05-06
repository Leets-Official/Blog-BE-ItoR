package com.blog.domain.post.service;

import com.blog.domain.post.controller.request.PostContentDto;
import com.blog.domain.post.domain.PostContent;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

public class PostContentMapper {

    public static PostContent mapFromDto(Long postId, PostContentDto postContentDto) {
        return switch (postContentDto.type()) {
            case TEXT -> PostContent.text(postId, postContentDto.data(), 0);
            case IMAGE -> PostContent.image(postId, postContentDto.data(), 0);
            default -> throw new CustomException(ErrorCode.POST_TYPE_NOT_FOUND);
        };
    }

    public static List<PostContent> mapFromDtos(Long postId, List<PostContentDto> postContentDtos) {
        return postContentDtos.stream()
                .map(dto -> mapFromDto(postId, dto))
                .collect(Collectors.toList());
    }
}
