package com.blog.user.presentation;

import com.blog.user.application.LoginService;
import com.blog.user.application.UserService;
import com.blog.user.presentation.dto.EmailLoginRequest;
import com.blog.user.presentation.dto.KakaoLoginRequest;
import com.blog.user.presentation.dto.UserResponse;
import com.blog.user.presentation.dto.UserSignupRequest;
import com.blog.token.presentation.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final Map<String, LoginService> loginStrategies;
    private final UserService userService;

    public UserController(List<LoginService> loginServices, UserService userService) {
        this.loginStrategies = loginServices.stream()
                .collect(Collectors.toMap(LoginService::getLoginType, Function.identity()));
        this.userService = userService;
    }

    @PostMapping("/email")
    public ResponseEntity<TokenResponse> loginByEmail(@RequestBody EmailLoginRequest request) {
        LoginService service = loginStrategies.get("email");
        if (service == null) {
            return ResponseEntity.badRequest().build();
        }

        TokenResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kakao")
    public ResponseEntity<TokenResponse> loginByKakao(@RequestBody KakaoLoginRequest request) {
        LoginService service = loginStrategies.get("kakao");
        if (service == null) {
            return ResponseEntity.badRequest().build();
        }

        TokenResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> signup(@RequestBody UserSignupRequest request) {
        UserResponse response = userService.signup(request);
        return ResponseEntity.ok(response);
    }
}
