package com.blog.workspace.application.service;

import com.blog.workspace.adapter.in.web.dto.request.UserRegisterRequest;
import com.blog.workspace.adapter.in.web.dto.request.UserUpdateRequest;
import com.blog.workspace.application.in.user.GetUserUseCase;
import com.blog.workspace.application.in.auth.RegisterUserUseCase;
import com.blog.workspace.application.in.user.UpdateUserUseCase;
import com.blog.workspace.application.out.image.ImagePort;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.application.service.exception.DuplicationUserException;
import com.blog.workspace.application.service.exception.NotSamePasswordException;
import com.blog.workspace.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService implements GetUserUseCase, RegisterUserUseCase, UpdateUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserPort userPort;

    /// 이미지 관련 의존성
    private final ImagePort imagePort;

    public UserService(UserPort userPort, ImagePort imagePort) {
        this.userPort = userPort;
        this.imagePort = imagePort;
    }


    /// 저장
    @Override
    public User registerUser(UserRegisterRequest request) throws IOException {

        /// 예외처리
        // 중복된 이메일 사용 불가
        if(userPort.loadExistingEmail(request.getEmail())){
            throw new DuplicationUserException("이미 존재하는 유저 입니다.");
        }

        // 비밀번호와 비밀번호 확인의 동일여부 체크
        validationPassWord(request.getPassword(), request.getPasswordCheck());

        /// 객체 생성
        LocalDateTime now = LocalDateTime.now();
        User user = createUser(request, now);

        // 회원가입
        return userPort.saveUser(user);
    }

    /// 함수 20줄 이하로 만들 위해 내부 함수로 사용
    private User createUser(UserRegisterRequest request, LocalDateTime now) throws IOException {

        String imageUrl = "";

        /// 이미지가 존재한다면 저장
        if (request.getImageUrl() != null) {
            imageUrl = createImage(request.getImageUrl());
        }

        return new User(
                request.getEmail(),
                request.getNickname(),
                request.getUsername(),
                request.getPassword(),
                imageUrl,
                false,
                request.getDescription(),
                request.getBirthday(),
                now,
                now
        );
    }

    /// 조회
    @Override
    public User getMyInfo(Long userId) {

        /// 예외 처리
        return userPort.findMe(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저는 존재하지 않습니다."));
    }


    /// 수정
    @Override
    public void updateUser(Long userId, UserUpdateRequest request) throws IOException {

        User user = userPort.findMe(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저는 수정할 수 없습니다."));

        ///  카카오 및 자체 유저의 수정하기
        changeUserInfo(request, user);

        /// 자체 유저 비밀번호 변경 처리, 카카오는 불가능하다.
        if (!user.isSocial()) {

            if (request.getPassword() != null && request.getPasswordCheck() != null) {
                // 비밀번호와 비밀번호 확인의 동일여부 체크
                validationPassWord(request.getPassword(), request.getPasswordCheck());

                user.changePassword(request.getPassword());
            }

        }

        userPort.updateUser(user);
    }


    private void validationPassWord(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new NotSamePasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    ///  유저 정보 수정
    private void changeUserInfo(UserUpdateRequest request, User user) throws IOException {

        // 닉네임이 요청에 있으면 수정
        if (request.getNickname() != null && !request.getNickname().isEmpty()) {
            user.changeNickname(request.getNickname());
        }

        // 생일이 요청에 있으면 수정
        if (request.getBirthday() != null && !request.getBirthday().isEmpty()) {
            user.changeBirthday(request.getBirthday());
        }

        // 설명이 요청에 있으면 수정
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            user.changeDescription(request.getDescription());
        }

        // 이미지 URL이 요청에 있으면 수정
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {

            String imageUrl = createImage(request.getImageUrl());
            user.changeImageUrl(imageUrl);
        }
    }

    /// 유저 이미지 저장
    private String createImage(MultipartFile file) throws IOException {
        return imagePort.uploadFiles(file);
    }




}
