package com.blog.workspace.application.in.user;

import com.blog.workspace.adapter.in.web.dto.request.UserUpdateRequest;

import java.io.IOException;

public interface UpdateUserUseCase {

    /*
        자체 로그인한 유저의 개인정보 수정 - [이미지, 닉네임, 한줄 소개,비밀번호, 생년월일]에 대한 수정이 가능하다.
        카카오 로그인 유저의 개인정보 수정 - [이미지, 닉네임, 한줄 소개, 생년월일]에 대해 수정이 가능하다.
     */

    void updateUser(Long userId, UserUpdateRequest request) throws IOException;
}
