package com.blog.global.security;

// 카톡 access token 재발급을 위해 필요
public class OAuthToken {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

    public int getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

    public String getScope() {
        return scope;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getAccess_token() {
        return access_token;
    }


}