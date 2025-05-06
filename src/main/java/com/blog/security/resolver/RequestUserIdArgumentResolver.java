package com.blog.security.resolver;

import com.blog.security.anotation.RequestUserId;
import com.blog.workspace.application.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class RequestUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;

    public RequestUserIdArgumentResolver(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        /// 파라미터 지원 여부 확인
        return parameter.hasParameterAnnotation(RequestUserId.class) &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        /// Casting을 통해서 HttpServletRequest으로 변환 후, 진행
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return tokenService.getUserIdFromToken(request);
    }
}
