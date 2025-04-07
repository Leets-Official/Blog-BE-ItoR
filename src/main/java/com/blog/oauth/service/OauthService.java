package com.blog.oauth.service;

import com.blog.oauth.vo.OauthVo;

public interface OauthService {
    OauthVo findByOauth(String provider, String providerUserId);
    void insertOauth(OauthVo oauthVo);


}//interface
