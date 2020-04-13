package com.alex.limiter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final HandlerInterceptor ipInterceptor;

    public WebMvcConfig(HandlerInterceptor ipInterceptor) {
        this.ipInterceptor = ipInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipInterceptor);
    }
}
