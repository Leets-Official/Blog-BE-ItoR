package com.blog.oauth.service;

import com.blog.oauth.mapper.OauthMapper;
import com.blog.oauth.vo.OauthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthServiceImpl implements OauthService {

    @Autowired
    private OauthMapper mapper;

    @Override
    public OauthVo findByOauth(String provider, String providerUserId) {
        return mapper.findByOauth(provider, providerUserId);
    }

    @Override
    public void insertOauth(OauthVo oauthVo) {
        mapper.insertOauth(oauthVo);
    }


}//class
