package com.blog.workspace.application.in.user;

import com.blog.workspace.domain.user.User;


public interface GetUserUseCase {

    /*
        사용자는 자신의 정보를 조회할 수 있어야 합니다.
        사용자는 닉네임, 비밀번호, 프로필 사진을 변경할 수 있어야 합니다.
     */

    User getMyInfo(Long userId);
}
