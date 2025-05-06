package com.blog.token.presentation;

import com.blog.token.domain.TokenService;
import com.blog.token.presentation.dto.RefreshTokenRequest;
import com.blog.token.presentation.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        TokenResponse response = tokenService.refresh(request);
        return ResponseEntity.ok(response);
    }
}
