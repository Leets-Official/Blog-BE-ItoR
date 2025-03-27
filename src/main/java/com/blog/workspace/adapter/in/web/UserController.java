package com.blog.workspace.adapter.in.web;

import com.blog.common.response.ApiResponse;
import com.blog.workspace.adapter.in.web.dto.response.UserResponse;
import com.blog.workspace.application.in.user.GetUserUseCase;
import com.blog.workspace.application.in.user.RegisterUserUseCase;
import com.blog.workspace.domain.user.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final GetUserUseCase getService;

    /// 생성자
    public UserController(GetUserUseCase getService, RegisterUserUseCase registerService) {
        this.getService = getService;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getMyInfo(@PathVariable Long userId) {
        User user = getService.getMyInfo(userId);

        return ApiResponse.ok(new UserResponse(user));
    }
}
