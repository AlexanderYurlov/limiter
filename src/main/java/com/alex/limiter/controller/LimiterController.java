package com.alex.limiter.controller;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.limiter.config.LimiterProperties;
import com.alex.limiter.service.LimitStorageService;
import com.alex.limiter.utils.IpUtils;

import static java.lang.System.currentTimeMillis;

@RestController
public class LimiterController {

    public static final String BASE_PATH = "";

    private final LimiterProperties limiterProperties;

    public LimiterController(LimiterProperties limiterProperties) {
        this.limiterProperties = limiterProperties;
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<?> method(HttpServletRequest request) {
        var ip = IpUtils.getRemoteAddr(request);
        if (!LimitStorageService.isAccessible(ip, currentTimeMillis(), limiterProperties)) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return ResponseEntity.ok(Collections.EMPTY_MAP);
    }

}
