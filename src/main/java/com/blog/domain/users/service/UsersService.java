package com.blog.domain.users.service;

import com.blog.common.EncryptUtils;
import com.blog.common.response.ApiResponse;
import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.request.AuthKaKaoRequest;
import com.blog.domain.auth.api.dto.response.AuthResponse;
import com.blog.domain.login.api.dto.request.LoginEmailRequest;
import com.blog.domain.users.api.dto.request.UsersIdRequest;
import com.blog.domain.users.api.dto.request.UsersUpdateRequest;
import com.blog.domain.users.api.dto.response.UsersInfoResponse;
import com.blog.domain.users.api.dto.response.UsersResultResponse;
import com.blog.domain.users.domain.Users;
import com.blog.domain.users.domain.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    // 회원가입 Service
    public AuthResponse addUserByEmail(AuthEmailRequest request) throws NoSuchAlgorithmException {

        // 비밀번호 암호화
        String hashedPassword = EncryptUtils.sha256(request.password());

        // 이메일 중복 확인
        if (usersRepository.isEmailDuplicated(request.email())) {
            return new AuthResponse(false, "이미 사용 중인 이메일입니다.", null);
        }

        // 닉네임 중복 확인
        if (usersRepository.isNickNameDuplicated(request.nickname())) {
            return new AuthResponse(false, "이미 사용 중인 닉네임입니다.", null);
        }
        // 중복이 없으면 회원가입 진행
        return new AuthResponse(true, "회원가입 성공", usersRepository.addUsersByEmail(request, hashedPassword));
    }


    // 비밀번호, 닉네임, 프로필 이미지 수정
    public ApiResponse<UsersResultResponse> userUpdateInfo(UsersUpdateRequest request){

        if (usersRepository.isNickNameDuplicated(request.nickname())) {
            return ApiResponse.error("업데이트 실패:  닉네임 중복");
        }

        int result = usersRepository.usersUpdateInfo(request);

        if (result == 0) {
            return ApiResponse.error("업데이트 실패: 사용자 정보 없음");
        }

        return ApiResponse.success(new UsersResultResponse(result));
    }

    // 사용자 정보 조회
    public ApiResponse<UsersInfoResponse> getUsersByUserId(UsersIdRequest request){

        UsersInfoResponse response = usersRepository.getUsersByUserId(request);
        // 조회 실패: 사용자 정보 없을 때
        if(response == null){
            return ApiResponse.error("조회 실패: 사용자 정보 없음");
        }
        // 조회 성공
        return ApiResponse.success(response);
    }

    // 사용자 삭제
    public ApiResponse<UsersResultResponse> usersDeleteInfo(UsersIdRequest request){
        UsersResultResponse response = usersRepository.usersDeleteInfo(request);

        // 삭제 실패
        if(response == null){
            return ApiResponse.error("조회 실패: 사용자 정보 없음");
        }
        // 삭제 성공
        return ApiResponse.success(response);
    }

    // 이메일 로그인
    public Users getUsersByEmailAndPassword(LoginEmailRequest request, String hashedPassword){
        return usersRepository.getUsersByEmailAndPassword(request, hashedPassword);
    }

    // 카카오 회원가입
    public AuthResponse addUserByKakao(AuthKaKaoRequest request, String name){

        // 닉네임 중복 확인
        if (usersRepository.isNickNameDuplicated(request.nickname())) {
            return new AuthResponse(false, "이미 사용 중인 닉네임입니다.", null);
        }

        // 중복이 없으면 회원가입 진행
        return new AuthResponse(true, "회원가입 성공", usersRepository.addUserByKaKao(request, name));
    }

    // 이메일, 이름 조회 - 이메일은 권한이 없어서 임시로 이름만 받아오기
    public Users getUsersByName(String name){

        return usersRepository.getUsersByName(name);
    }
}

