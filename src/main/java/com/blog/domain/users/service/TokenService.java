package com.blog.domain.users.service;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.global.security.jwt.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtUtil jwtUtil;

    public TokenService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public int extractUserIdFromHeader(String authHeader) {
        String token = parseToken(authHeader);

        if (!jwtUtil.validateToken(token)) {

            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return jwtUtil.getUserIdFromToken(token);
    }

    private String parseToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return authHeader.replace("Bearer ", "");
    }

}
