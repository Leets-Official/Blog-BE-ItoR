package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.request.AuthFirstTimeRequest;
import com.blog.workspace.adapter.in.web.dto.request.UserRegisterRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import com.blog.workspace.adapter.out.oauth.kakao.KaKaoLoginParam;
import com.blog.workspace.application.in.auth.AuthUserUseCase;
import com.blog.workspace.application.in.auth.RegisterUserUseCase;
import com.blog.workspace.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // 자체 회원가입
    private final RegisterUserUseCase registerService;

    // 카카오 로그인
    private final AuthUserUseCase authService;

    /// 생성자
    public AuthController(RegisterUserUseCase registerService, AuthUserUseCase authService) {
        this.registerService = registerService;
        this.authService = authService;
    }

    /// 자체 회원가입 기능
    @PostMapping
    public ApiResponse<String> register(@RequestBody @Valid UserRegisterRequest request) {

        registerService.registerUser(request);
        return ApiResponse.ok("회원 가입 성공했습니다.");
    }

    /// 프런트에서 요청한 code 콜백 함수 및 완성 함수
    @GetMapping("/code/kakao")
    public ApiResponse<UserLoginResponse> kakaoCallBack(@RequestParam String code) {

        // 전달된 code를 이용해 카카오 토큰 요청
        KaKaoLoginParam param = new KaKaoLoginParam(code);

        // 토큰을 바탕으로 authService를 통해 로그인 처리
        return ApiResponse.ok(authService.login(param));
    }

    // 최초 로그인 시, 유저의 추가 정보 기입
    @PutMapping("/{email}")
    public ApiResponse<User> updateUser(
            @PathVariable String email,
            @RequestBody @Valid AuthFirstTimeRequest request) {
        User user = authService.updateInfo(email, request);

        return ApiResponse.ok(user);
    }

}
