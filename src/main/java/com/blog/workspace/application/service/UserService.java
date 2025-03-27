package com.blog.workspace.application.service;

import com.blog.workspace.adapter.in.web.dto.request.UserRegisterRequest;
import com.blog.workspace.application.in.user.GetUserUseCase;
import com.blog.workspace.application.in.user.RegisterUserUseCase;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.application.service.exception.DuplicationUserException;
import com.blog.workspace.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService implements GetUserUseCase, RegisterUserUseCase {

    private final UserPort userPort;

    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }


    /// 저장
    @Override
    public User registerUser(UserRegisterRequest request) {

        // 객체 생성
        LocalDateTime now = LocalDateTime.now();
        User user = createUser(request, now);

        /// 예외처리
        // 중복된 이메일 사용 불가
        if(userPort.loadExistingEmail(request.getEmail())){
            throw new DuplicationUserException("이미 존재하는 유저 입니다.");
        }

        // 비밀번호와 비밀번호 확인의 동일여부 체크, 패스워드 암호화 처리도 진행해야..
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다”");
        }

        // 회원가입
        return userPort.saveUser(user);
    }

    /// 조회
    @Override
    public User getMyInfo(Long userId) {

        /// 예외 처리
        return userPort.findMe(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저는 존재하지 않습니다."));
    }


    /// 함수 20줄 이하로 만들 위해 내부 함수로 사용
    private User createUser(UserRegisterRequest request, LocalDateTime now) {
        return new User(
                request.getEmail(),
                request.getNickname(),
                request.getUsername(),
                request.getPassword(),
                request.getImageUrl(),
                false,
                request.getDescription(),
                request.getBirthday(),
                now,
                now
        );
    }


}
