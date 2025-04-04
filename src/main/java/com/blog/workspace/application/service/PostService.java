package com.blog.workspace.application.service;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.workspace.adapter.in.web.dto.request.ContentRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostUpdateRequest;
import com.blog.workspace.adapter.in.web.dto.response.PostDetailResponse;
import com.blog.workspace.adapter.in.web.dto.response.PostListResponse;
import com.blog.workspace.adapter.in.web.dto.response.UserPostResponse;
import com.blog.workspace.application.in.post.PostUseCase;
import com.blog.workspace.application.out.post.ContentBlockPort;
import com.blog.workspace.application.out.post.DeletePostPort;
import com.blog.workspace.application.out.post.LoadPostPort;
import com.blog.workspace.application.out.post.SavePostPort;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.application.service.exception.NotEqualPostDeleteException;
import com.blog.workspace.application.service.exception.NotEqualPostUpdateException;
import com.blog.workspace.application.service.exception.NotRequestException;
import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.ContentType;
import com.blog.workspace.domain.post.Post;
import com.blog.workspace.domain.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Post savePost(PostRequest request, Long userId) {

        /// 유저 검증
        userPort.findMe(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        List<ContentRequest> contents = request.getContent();

        /// 게시글 자체 저장
        LocalDateTime now = LocalDateTime.now();
        Post post = Post.of(userId, request.getTitle(), now, now);
        Post savedPost = savePort.savePost(post);

        /// 내용 블럭 저장
        // 순서를 위한 변수 설정
        int ord = 0;
        createContenetBlock(contents, savedPost, ord);

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
    public Page<PostListResponse> loadPosts(Pageable pageable, Long userId) {
        Page<Post> result = loadPort.loadPosts(pageable, userId);
        List<Post> content = result.getContent();

        List<PostListResponse> arrayList = new ArrayList<>();

        content.forEach(post -> {

            /// 유저 정보 처리
            User user = userPort.findMe(post.getUserId()).orElseGet(null);
            UserPostResponse userResponse = UserPostResponse.from(user);

            /// 썸네일 및 내용 처리
            List<ContentBlock> contentBlocks = contentPort.loadBlocks(post.getId());
            String contentBlock = String.valueOf(contentBlocks.get(0).getContent());

            String thumbnail = contentBlocks.stream()
                    .filter(block -> block.getType() == ContentType.IMAGE)
                    .findFirst()
                    .map(ContentBlock::getContent)
                    .orElse(null);

            PostListResponse response = PostListResponse.from(post, userResponse, contentBlock, thumbnail);
            arrayList.add(response);
        });

        return new Page<>(arrayList, pageable, arrayList.size());
    }

    // 게시글 수정
    @Override
    public Post updatePost(Long postId, Long userId, PostUpdateRequest request) {

        /// 제목과 내용이 모두 비어 있는 경우 예외 처리
        if ((request.getTitle() == null || request.getTitle().trim().isEmpty()) &&
                (request.getContent() == null || request.getContent().isEmpty())) {
            throw new NotRequestException("제목, 내용 중 하나는 수정해야 합니다.");
        }

        /// 게시글 가져오기
        Post post = loadPort.loadPost(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다."));

        /// 유저 검증 조건 처리
        if (!post.getUserId().equals(userId)) {
            throw new NotEqualPostUpdateException("글 작성자가 아니기에 게시글을 수정할 수 없습니다.");
        }

        ///  글 수정하기
        changePost(request, post);
        return savePort.updatePost(post);
    }


    // 게시글 삭제
    @Override
    public void deletePost(Long userId, Long postId) {

        /// 본인 게시글인지 예외처리
        boolean checked = loadPort.checkPostByUserId(userId, postId);

        if (!checked) {
            throw new NotEqualPostDeleteException("게시글 작성자와 삭제 요청자가 서로 다릅니다.");
        }

        // 게시글 자체 삭제
        deletePort.deletePostById(postId);

        // 관련 내용 삭제
        contentPort.deleteBlockByPost(postId);
    }

    /// 내부 함수
    // 블록 생성
    private void createContenetBlock(List<ContentRequest> contents, Post savedPost, int ord) {
        for (ContentRequest content : contents) {
            ContentBlock contentBlock = ContentBlock.of(savedPost.getId(), content.getType(), content.getContent(), ++ord);
            contentPort.saveBlock(contentBlock);
        }
    }

    // 글 수정하기
    private void changePost(PostUpdateRequest request, Post post) {
        // 제목이 요청에 있으면 수정
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            post.changeTitle(request.getTitle());
        }

        // 글이 요청에 있으면 수정, 기존 블록은 삭제하고 다시 저장
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            contentPort.deleteBlockByPost(post.getId());

            // 새롭게 내용을 저장
            createContenetBlock(request.getContent(), post, 0);
        }
    }


}
