package com.blog.workspace.application.service;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.workspace.adapter.in.web.dto.request.ContentRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostUpdateRequest;
import com.blog.workspace.adapter.in.web.dto.response.CommentResponse;
import com.blog.workspace.adapter.in.web.dto.response.PostDetailResponse;
import com.blog.workspace.adapter.in.web.dto.response.PostListResponse;
import com.blog.workspace.adapter.in.web.dto.response.UserPostResponse;
import com.blog.workspace.application.in.comment.CommentUseCase;
import com.blog.workspace.application.in.post.PostUseCase;
import com.blog.workspace.application.out.comment.LoadCommentPort;
import com.blog.workspace.application.out.image.ImagePort;
import com.blog.workspace.application.out.post.ContentBlockPort;
import com.blog.workspace.application.out.post.DeletePostPort;
import com.blog.workspace.application.out.post.LoadPostPort;
import com.blog.workspace.application.out.post.SavePostPort;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.application.service.exception.NotEqualDeleteException;
import com.blog.workspace.application.service.exception.NotEqualUpdateException;
import com.blog.workspace.application.service.exception.NotRequestException;
import com.blog.workspace.domain.comment.Comment;
import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.ContentType;
import com.blog.workspace.domain.post.Post;
import com.blog.workspace.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class PostService implements PostUseCase {

    private final SavePostPort savePort;
    private final LoadPostPort loadPort;
    private final DeletePostPort deletePort;

    // 의존성
    private final ContentBlockPort contentPort;
    private final LoadCommentPort commentPort;

    private final UserPort userPort;
    private final ImagePort imagePort;

    /// 생성자
    public PostService(SavePostPort savePort, LoadPostPort loadPort, DeletePostPort deletePort, ContentBlockPort contentPort, LoadCommentPort commentPort, UserPort userPort, CommentUseCase commentUseCase, ImagePort imagePort) {
        this.savePort = savePort;
        this.loadPort = loadPort;
        this.deletePort = deletePort;
        this.contentPort = contentPort;
        this.commentPort = commentPort;
        this.userPort = userPort;
        this.imagePort = imagePort;
    }

    @Override
    public Post savePost(PostRequest request, Long userId) throws IOException {

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
        createContentBlock(contents, savedPost, ord);

        return savedPost;
    }

    // 게시글 상세 조회
    @Override
    public PostDetailResponse loadPostById(Long id) {

        // 게시글 예외처리 적용
        Post post = loadPort.loadPost(id)
                .orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 게시글이 존재하지 않습니다."));

        // 게시글 작성자 조회
        User user = getUser(post.getUserId());

        // 컨텐츠 블록 추가하기
        List<ContentBlock> contents = contentPort.loadBlocks(post.getId());

        // 댓글 추가하기
        List<CommentResponse> comments = getComments(post.getId());

        /// 응답 DTO 
        PostDetailResponse response = PostDetailResponse.from(post, user, contents);
        response.changeComments(comments);
        response.changeCommentCount(comments.size());

        return response;
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

            /// 댓글 개수 처리
            List<Comment> comments = commentPort.loadCommentsByPostId(post.getId());
            int commentCount = comments.size();

            PostListResponse response = PostListResponse.from(post, userResponse, contentBlock, thumbnail, commentCount);
            arrayList.add(response);
        });

        return new Page<>(arrayList, pageable, arrayList.size());
    }

    // 게시글 수정
    @Override
    public Post updatePost(Long postId, Long userId, PostUpdateRequest request) throws IOException {

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
            throw new NotEqualUpdateException("글 작성자가 아니기에 게시글을 수정할 수 없습니다.");
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
            throw new NotEqualDeleteException("게시글 작성자와 삭제 요청자가 서로 다릅니다.");
        }

        // 게시글 자체 삭제
        deletePort.deletePostById(postId);

        // 관련 내용 삭제
        contentPort.deleteBlockByPost(postId);
    }

    /// 내부 함수
    // 블록 생성
    private void createContentBlock(List<ContentRequest> contents, Post savedPost, int ord) throws IOException {
        for (ContentRequest content : contents) {

            String blockContent;

            if (content.getType() == ContentType.IMAGE) {
                blockContent = imagePort.uploadFiles(content.getImage());
            } else {
                blockContent = content.getContent();
            }

            ContentBlock contentBlock = ContentBlock.of(savedPost.getId(), content.getType(), blockContent, ++ord);
            contentPort.saveBlock(contentBlock);
        }
    }

    // 글 수정하기
    private void changePost(PostUpdateRequest request, Post post) throws IOException {
        // 제목이 요청에 있으면 수정
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            post.changeTitle(request.getTitle());
        }

        // 글이 요청에 있으면 수정, 기존 블록은 삭제하고 다시 저장
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            contentPort.deleteBlockByPost(post.getId());

            // 새롭게 내용을 저장
            createContentBlock(request.getContent(), post, 0);
        }
    }

    /// 내부 함수
    private User getUser(Long userId) {
        return userPort.findMe(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }

    private List<CommentResponse> getComments(Long postId) {

        /// 초기 설정
        List<CommentResponse> commentResponses = new ArrayList<>();

        /// 가져오기
        List<Comment> comments = commentPort.loadCommentsByPostId(postId);

        if (comments.isEmpty()){
            return commentResponses;
        }

        comments.forEach(comment -> {
            /// 유저 정보 처리
            User commentUser = getUser(comment.getUserId());

            /// 댓글 내용 처리
            CommentResponse response = CommentResponse.from(comment, commentUser);
            commentResponses.add(response);
        });

        return commentResponses;
    }

}
