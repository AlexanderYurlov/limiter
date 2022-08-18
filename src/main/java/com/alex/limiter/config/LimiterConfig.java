package com.alex.limiter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties(LimiterProperties.class)
@EnableScheduling
@Slf4j
public class LimiterConfig {

    private final LimiterProperties limiterProperties;

    public LimiterConfig(LimiterProperties limiterProperties) {
        this.limiterProperties = limiterProperties;
    }

}
