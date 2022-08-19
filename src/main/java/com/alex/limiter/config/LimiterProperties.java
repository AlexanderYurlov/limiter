package com.alex.limiter.config;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Positive;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties("limiter.limit")
public class LimiterProperties {

    private static final Integer MLS_IN_MIN = 60000;

    @Positive
    private Integer timePeriodMin;

    @Positive
    private Integer maxCallQuantity;

    private Long timePeriodMls;

    @PostConstruct
    public void afterPropertiesSet() {
        timePeriodMls = (long) (timePeriodMin * MLS_IN_MIN);
    }
}
