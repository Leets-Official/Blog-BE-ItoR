package com.blog.token.domain;

import com.blog.token.presentation.dto.RefreshTokenRequest;
import com.blog.token.presentation.dto.TokenResponse;

public interface TokenService {
    TokenResponse refresh(RefreshTokenRequest request);
}
