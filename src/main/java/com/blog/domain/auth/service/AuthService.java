package com.blog.domain.auth.service;

import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.response.AuthEmailResponse;
import com.blog.domain.users.service.UsersService;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersService usersService;

    public AuthService(UsersService usersService){
        this.usersService = usersService;
    }

    public AuthEmailResponse emailRegister(AuthEmailRequest request){
        return usersService.emailRegister(request);
    }
}
