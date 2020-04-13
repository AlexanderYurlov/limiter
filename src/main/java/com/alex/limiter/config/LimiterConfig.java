package com.alex.limiter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

import com.alex.limiter.config.interceptor.SessionData;

@Configuration
@EnableConfigurationProperties(LimiterProperties.class)
@EnableScheduling
@Slf4j
public class LimiterConfig {

    private final LimiterProperties limiterProperties;

    public LimiterConfig(LimiterProperties limiterProperties) {
        this.limiterProperties = limiterProperties;
    }

    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public SessionData requestSessionData() {
        return new SessionData();
    }
}
