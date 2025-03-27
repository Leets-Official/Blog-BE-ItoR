package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.request.UserUpdateRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserResponse;
import com.blog.workspace.application.in.user.GetUserUseCase;
import com.blog.workspace.application.in.user.RegisterUserUseCase;
import com.blog.workspace.application.in.user.UpdateUserUseCase;
import com.blog.workspace.domain.user.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final GetUserUseCase getService;
    private final UpdateUserUseCase updateService;

    /// 생성자
    public UserController(GetUserUseCase getService, RegisterUserUseCase registerService, UpdateUserUseCase updateService) {
        this.getService = getService;
        this.updateService = updateService;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getMyInfo(@PathVariable Long userId) {
        User user = getService.getMyInfo(userId);

        return ApiResponse.ok(new UserResponse(user));
    }

    @PutMapping("/{userId}")
    public ApiResponse<String> updateMyInfo(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {

        updateService.updateUser(userId, request);
        return ApiResponse.ok("수정되었습니다.");
    }

}
