package com.blog.domain.posts.service;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.posts.api.dto.request.PostBlockRequest;
import com.blog.domain.posts.api.dto.response.PostBlockResponse;
import com.blog.domain.posts.api.dto.response.PostSummary;
import com.blog.domain.posts.domain.PostBlocks;
import com.blog.domain.posts.domain.repository.PostBlocksRepository;
import com.blog.domain.posts.domain.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<PostSummary> mapPostBlocks(List<PostSummary> posts) {
        if (posts.isEmpty()) return Collections.emptyList();

        Map<Integer, PostSummary> postMap = posts.stream()
                .collect(Collectors.toMap(PostSummary::postId, post -> post));

        List<Integer> postIds = new ArrayList<>(postMap.keySet());

        List<PostBlockResponse> blocks = postBlockRepository.findPostBlocks(postIds);

        Map<Integer, List<PostBlockResponse>> blockMap = blocks.stream()
                .collect(Collectors.groupingBy(block -> findPostId(block, posts)));

        // 게시글에 블록 정보를 매핑
        return posts.stream()
                .map(post -> {
                    // 해당 게시글의 블록 리스트 가져오기
                    List<PostBlockResponse> postBlocks = blockMap.getOrDefault(post.postId(), List.of());
                    return new PostSummary(
                            post.postId(),
                            post.nickname(),
                            post.subject(),
                            postBlocks,
                            post.createdAt()
                    );
                })
                .collect(Collectors.toList());
    }

    private int findPostId(PostBlockResponse block, List<PostSummary> posts) {
        return posts.stream()
                .filter(post -> post.block().contains(block))
                .findFirst()
                .map(PostSummary::postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST_ID));
    }
}
