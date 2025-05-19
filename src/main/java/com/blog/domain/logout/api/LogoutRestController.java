package com.blog.domain.logout.api;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.common.response.ResponseCode;
import com.blog.domain.logout.service.LogoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutRestController {

    private final LogoutService logoutService;

    public LogoutRestController(LogoutService logoutService){
        this.logoutService = logoutService;
    }

    @PostMapping
    public ApiResponse<String> userLogout(
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        if (authorization == null || authorization.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String accessToken = authorization.substring(7);
        logoutService.signOut(accessToken);

        return ApiResponse.ok(ResponseCode.LOGOUT_SUCCESS);
    }
}
