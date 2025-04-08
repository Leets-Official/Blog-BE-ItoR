package com.blog.domain.posts.service;

import com.blog.domain.posts.api.dto.request.PostBlockRequest;
import com.blog.domain.posts.domain.PostBlocks;
import com.blog.domain.posts.domain.repository.PostBlocksRepository;
import com.blog.domain.posts.domain.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostBlockService {
    private final PostBlocksRepository postBlockRepository;

    public PostBlockService(PostBlocksRepository postBlockRepository) {
        this.postBlockRepository = postBlockRepository;
    }

    // 단락 저장
    public void saveAllBlocks(int postId, List<PostBlockRequest> blocks) {
        for (PostBlockRequest block : blocks) {
            PostBlocks postBlock = PostBlocks.createPostBlock(postId, block.content(), block.imageUrl());

            postBlockRepository.addPostBlock(postBlock);
        }
    }

    // 단락 조회
    public List<PostBlocks> getPostBlockListByPostId(int postId){

        return postBlockRepository.getPostBlockListByPostId(postId);
    }

}
