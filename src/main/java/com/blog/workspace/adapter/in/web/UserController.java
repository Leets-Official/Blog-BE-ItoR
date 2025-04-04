package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.request.UserUpdateRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserResponse;
import com.blog.workspace.application.in.user.GetUserUseCase;
import com.blog.workspace.application.in.user.UpdateUserUseCase;
import com.blog.workspace.application.service.TokenService;
import com.blog.workspace.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final GetUserUseCase getService;
    private final UpdateUserUseCase updateService;

    private final TokenService tokenService;
    /// 생성자
    public UserController(GetUserUseCase getService, UpdateUserUseCase updateService, TokenService tokenService) {
        this.getService = getService;
        this.updateService = updateService;
        this.tokenService = tokenService;
    }

    @GetMapping()
    public ApiResponse<UserResponse> getMyInfo(HttpServletRequest httpServletRequest) {

        /// 토큰에서 유저Id 가져오기
        Long userId = tokenService.getUserIdFromToken(httpServletRequest);

        User user = getService.getMyInfo(userId);

        return ApiResponse.ok(new UserResponse(user));
    }

    @PutMapping()
    public ApiResponse<String> updateMyInfo(HttpServletRequest httpServletRequest, @RequestBody UserUpdateRequest request) {

        /// 토큰에서 유저Id 가져오기
        Long userId = tokenService.getUserIdFromToken(httpServletRequest);

        updateService.updateUser(userId, request);
        return ApiResponse.ok("수정되었습니다.");
    }

}
