package com.blog.global.config;

import com.blog.global.security.TokenFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    // 필터를 등록하는 FilterRegistraionBean
    //setFilter로 jwtfilter적용
    // 순서를 1번으로 적용
    // 모든 경우에 이 필터를 적용하고 적용하면 안되는 걸 필터 내부에 정의
    @Bean
    public FilterRegistrationBean<Filter> jwtFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new TokenFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}