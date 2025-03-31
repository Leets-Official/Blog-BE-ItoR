package com.blog.domain.users.api;

import com.blog.common.response.ApiResponse;
import com.blog.domain.users.api.dto.request.UsersIdRequest;
import com.blog.domain.users.api.dto.request.UsersUpdateRequest;
import com.blog.domain.users.api.dto.response.UsersDeleteResponse;
import com.blog.domain.users.api.dto.response.UsersInfoResponse;
import com.blog.domain.users.api.dto.response.UsersUpdateResponse;
import com.blog.domain.users.service.UsersService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersRestController {

    private final UsersService usersService;

    public UsersRestController(UsersService usersService){
        this.usersService = usersService;
    }

    // 닉네임, 비밀번호, 프로필 사진을 변경
    @PatchMapping("/update")
    public ApiResponse<UsersUpdateResponse> usersUpdateInfo(
            @RequestBody UsersUpdateRequest request){
        return usersService.userUpdateInfo(request);
    }

    // 자신의 정보를 조회
    @GetMapping("/info")
    public ApiResponse<UsersInfoResponse> usersInfo(
            @RequestBody UsersIdRequest request){
        return usersService.usersInfo(request);
    }

    // 사용자 정보 삭제
    @DeleteMapping("/delete")
    public ApiResponse<UsersDeleteResponse> usersDeleteInfo(
            @RequestBody UsersIdRequest request){
        return usersService.usersDeleteInfo(request);
    }
}
