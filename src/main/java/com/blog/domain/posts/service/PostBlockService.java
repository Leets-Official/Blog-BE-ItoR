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

    // 단락 수정
    public void updatePostBlock(int postId, List<PostBlockRequest> blocks){
        List<PostBlocks> postBlockIdList = postBlockRepository.getPostBlockListWithIdByPostId(postId);

        // 기존 블록 수정 및 새로운 블록 추가
        for (int i = 0; i < blocks.size(); i++) {
            PostBlockRequest block = blocks.get(i);
            if (i < postBlockIdList.size()) {
                updateExistingPostBlock(postBlockIdList.get(i), block);
            } else {
                addNewPostBlock(postId, block);
            }
        }

        // 삭제된 블록 처리
        if (blocks.size() < postBlockIdList.size()) {
            deleteRemovedPostBlocks(postBlockIdList, blocks.size());
        }
    }

    private void updateExistingPostBlock(PostBlocks postBlock, PostBlockRequest block) {
        postBlock.changeContent(block.content());
        postBlock.changeImageUrl(block.imageUrl());

        postBlockRepository.updatePostBlock(postBlock.getPostBlocksId(), postBlock);
    }

    private void addNewPostBlock(int postId, PostBlockRequest block) {
        PostBlocks newPostBlock = PostBlocks.createPostBlock(postId, block.content(), block.imageUrl());

        postBlockRepository.addPostBlock(newPostBlock);
    }

    private void deleteRemovedPostBlocks(List<PostBlocks> postBlockIdList, int startIndex) {
        for (int i = startIndex; i < postBlockIdList.size(); i++) {

            postBlockRepository.deletePostBlock(postBlockIdList.get(i).getPostBlocksId());
        }
    }

    // 단락 삭제
    public void deletePostBlock(int postId){

        postBlockRepository.deletePostBlockByPostId(postId);
    }
}
