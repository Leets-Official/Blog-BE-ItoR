package com.blog.workspace.application.service;

import com.blog.workspace.adapter.in.web.dto.request.CommentRequest;
import com.blog.workspace.application.domain.CommentFixtures;
import com.blog.workspace.application.domain.PostFixtures;
import com.blog.workspace.application.out.comment.SaveCommentPort;
import com.blog.workspace.application.out.post.LoadPostPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    /*
        Port가 정상작동한다는 가정 하에
        Service만 테스트하는 Unit 테스트입니다.
     */

    @Mock
    private SaveCommentPort savePort;


    @Mock
    private LoadPostPort postPort;

    @InjectMocks
    private CommentService sut;


    @Nested
    @DisplayName("생성 관련 테스트 입니다.")
    class createComment {
        @Test
        @DisplayName("[happy] 생성 관련 테스트")
        void createComment() {

            // Given
            Long userId = 1L;
            Long postId = 100L;
            var request = new CommentRequest(postId, "테스트");

            given(postPort.loadPost(request.postId()))
                    .willReturn(Optional.of(PostFixtures.stub(postId)));

            given(savePort.saveComment(any()))
                    .willReturn(CommentFixtures.stub(postId));

            // When
            var result = sut.saveComment(request, userId);

            // Then
            then(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("postId", postId)
                    .hasFieldOrPropertyWithValue("userId", userId);

        }
    }


}
