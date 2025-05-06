package com.blog.domain.post.service;

import com.blog.domain.post.domain.Post;
import com.blog.domain.post.repository.PostRepository;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;

public class PostValidator {

    // 유저 검증
    public static User validateUser(UserRepository userRepo, Long userId) {
        return userRepo.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 게시글 검증 및 소유자 검증을 한번에 처리
    public static Post validatePostAccess(PostRepository postRepo, Long postId, Long userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_POST_ACCESS);
        }

        return post;
    }
}
