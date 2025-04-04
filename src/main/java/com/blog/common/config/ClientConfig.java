package com.blog.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    /*
        Http 요청을 위한 restTemplate 설정
     */

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}