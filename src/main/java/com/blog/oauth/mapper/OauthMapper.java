package com.blog.oauth.mapper;

import com.blog.oauth.vo.OauthVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OauthMapper {

    OauthVo findByOauth(String provider, String providerUserId);
    void insertOauth(OauthVo oauthVo);


}//interface
