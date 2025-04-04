package com.blog.workspace.application.service;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.workspace.adapter.in.web.dto.request.ContentRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostRequest;
import com.blog.workspace.adapter.in.web.dto.response.PostDetailResponse;
import com.blog.workspace.application.in.post.PostUseCase;
import com.blog.workspace.application.out.post.ContentBlockPort;
import com.blog.workspace.application.out.post.DeletePostPort;
import com.blog.workspace.application.out.post.LoadPostPort;
import com.blog.workspace.application.out.post.SavePostPort;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.application.service.exception.NotEqualPostDeleteException;
import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.Post;
import com.blog.workspace.domain.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class PostService implements PostUseCase {

    private static final Logger log = LogManager.getLogger(PostService.class);
    private final SavePostPort savePort;
    private final LoadPostPort loadPort;
    private final DeletePostPort deletePort;

    // 의존성
    private final ContentBlockPort contentPort;
    private final UserPort userPort;

    /// 생성자
    public PostService(SavePostPort savePort, LoadPostPort loadPort, DeletePostPort deletePort, ContentBlockPort contentPort, UserPort userPort) {
        this.savePort = savePort;
        this.loadPort = loadPort;
        this.deletePort = deletePort;
        this.contentPort = contentPort;
        this.userPort = userPort;
    }

    @Override
    public Post savePost(PostRequest request) {

        /// 유저 검증
        userPort.findMe(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        List<ContentRequest> contents = request.getContent();

        /// 게시글 자체 저장
        LocalDateTime now = LocalDateTime.now();
        Post post = Post.of(request.getUserId(), request.getTitle(), now, now);
        Post savedPost = savePort.savePost(post);

        log.info("로그{}", savedPost.getId());

        /// 내용 블럭 저장
        // 순서를 위한 변수 설정
        int ord = 0;

        for (ContentRequest content : contents) {
            ContentBlock contentBlock = ContentBlock.of(savedPost.getId(), content.getType(), content.getContent(), ++ord);
            contentPort.saveBlock(contentBlock);
        }

        return savedPost;
    }

    // 게시글 상세 조회
    @Override
    public PostDetailResponse loadPostById(Long id) {

        // 게시글 예외처리 적용
        Post post = loadPort.loadPost(id)
                .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 게시글이 존재하지 않습니다."));

        // 게시글 작성자 조회
        User user = userPort.findMe(post.getUserId())
                .orElseThrow(() -> new NoSuchElementException("해당 게시글을 작성한 유저가 존재하지 않습니다."));

        // 컨텐츠 블록 추가하기
        List<ContentBlock> contents = contentPort.loadBlocks(post.getId());
        return PostDetailResponse.from(post, user, contents);
    }

    // 게시글 목록 조회
    @Override
    public Page<Post> loadPosts(Pageable pageable, Long userId) {
        return null;
    }

    // 게시글 수정
    @Override
    public Post updatePost(Post updatePost, Long userId) {
        return null;
    }

    // 게시글 삭제
    @Override
    public void deletePost(Long id, Long userId) {

        /// 본인 게시글인지 예외처리
        boolean checked = loadPort.checkPostByUserId(id, userId);

        if (!checked) {
            throw new NotEqualPostDeleteException("게시글 작성자와 삭제 요청자가 서로 다릅니다.");
        }

        // 게시글 자체 삭제
        deletePort.deletePostById(id);

        // 관련 내용 삭제
        contentPort.deleteBlockByPost(id);
    }




}
