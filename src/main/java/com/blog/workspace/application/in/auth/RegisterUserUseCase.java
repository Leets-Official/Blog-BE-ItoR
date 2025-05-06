package com.blog.workspace.application.in.auth;

import com.blog.workspace.adapter.in.web.dto.request.UserRegisterRequest;
import com.blog.workspace.domain.user.User;

import java.io.IOException;

public interface RegisterUserUseCase {

    /*
       자체 회원가입과 카카오 회원가입의 Port 구분하였습니다.
       사용자는 이메일 주소통해 회원가입을 진행할 수 있어야 합니다.
       [이름,이메일, 비밀번호 , 비밀번호 확인, 생년월일, 닉네임, 한줄 소개]를 입력 해야합니다.
       중복된 이메일 사용 불가
     */

    // 자체 회원가입
    User registerUser(UserRegisterRequest request) throws IOException;

}
