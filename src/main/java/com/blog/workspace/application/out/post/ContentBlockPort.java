package com.blog.workspace.application.out.post;

import com.blog.workspace.domain.post.ContentBlock;

import java.util.*;

public interface ContentBlockPort {

    // 저장
    ContentBlock saveBlock(ContentBlock contentBlock);

    // 조회
    List<ContentBlock> loadBlocks(Long postId);

    // 삭제
    void deleteBlockById(Long id);

    // 게시글 삭제와 함께 전부 삭제
    void deleteBlockByPost(Long postId);

    // 순서 변경하기
    void changeBlockOrder(ContentBlock contentBlock);

}
