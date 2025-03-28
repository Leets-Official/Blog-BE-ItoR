package com.blog.workspace.application.out.auth;

import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {

    /*
        OAuth 플랫폼에 요청하기 위한 파라미터 값들을 담는 역할
     */

    MultiValueMap<String, String> makeBody();

}
