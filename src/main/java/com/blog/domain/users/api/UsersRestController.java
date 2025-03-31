package com.blog.domain.users.api;

import com.blog.common.response.ApiResponse;
import com.blog.domain.auth.api.dto.response.AuthEmailResponse;
import com.blog.domain.users.api.dto.request.UsersInfoRequest;
import com.blog.domain.users.api.dto.request.UsersUpdateRequest;
import com.blog.domain.users.api.dto.response.UsersInfoResponse;
import com.blog.domain.users.api.dto.response.UsersUpdateResponse;
import com.blog.domain.users.service.UsersService;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersRestController {

    private final UsersService usersService;

    public UsersRestController(UsersService usersService){
        this.usersService = usersService;
    }

    // 닉네임, 비밀번호, 프로필 사진을 변경
    public ApiResponse<UsersUpdateResponse> usersUpdateInfo(
            @RequestBody UsersUpdateRequest request){
        return usersService.userUpdateInfo(request);
    }

    // 자신의 정보를 조회
    public ApiResponse<UsersInfoResponse> usersInfo(
            @RequestBody UsersInfoRequest request){
        return usersService.usersInfo(request);
    }
}
