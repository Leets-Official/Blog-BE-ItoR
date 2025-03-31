package com.blog.domain.users.service;

import com.blog.common.response.ApiResponse;
import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.response.AuthEmailResponse;
import com.blog.domain.users.api.dto.request.UsersInfoRequest;
import com.blog.domain.users.api.dto.request.UsersUpdateRequest;
import com.blog.domain.users.api.dto.response.UsersInfoResponse;
import com.blog.domain.users.api.dto.response.UsersUpdateResponse;
import com.blog.domain.users.domain.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    // 회원가입 Service
    public AuthEmailResponse emailRegister(AuthEmailRequest request) {

        // 이메일 중복 확인
        if (usersRepository.isEmailDuplicated(request)) {
            return new AuthEmailResponse(false, "이미 사용 중인 이메일입니다.", null);
        }

        // 닉네임 중복 확인
        if (usersRepository.isNickNameDuplicated(request.nickname())) {
            return new AuthEmailResponse(false, "이미 사용 중인 닉네임입니다.", null);
        }
        // 중복이 없으면 회원가입 진행
        return new AuthEmailResponse(true, "회원가입 성공", usersRepository.emailRegister(request));
    }


    // 비밀번호, 닉네임, 프로필 이미지 수정
    public ApiResponse<UsersUpdateResponse> userUpdateInfo(UsersUpdateRequest request){

        if (usersRepository.isNickNameDuplicated(request.nickname())) {
            return ApiResponse.error("업데이트 실패:  닉네임 중복");
        }

        int result = usersRepository.usersUpdateInfo(request);

        if (result == 0) {
            return ApiResponse.error("업데이트 실패: 사용자 정보 업슴");
        }

        return ApiResponse.success(new UsersUpdateResponse(result));
    }

    // 사용자 정보 조회
    public ApiResponse<UsersInfoResponse> usersInfo(UsersInfoRequest request){

        UsersInfoResponse response = usersRepository.usersInfo(request);
        // 조회 실패: 사용자 정보 없을 때
        if(response == null){
            return ApiResponse.error("조회 실패: 사용자 정보 업슴");
        }
        // 조회 성공
        return ApiResponse.success(response);
    }
}

