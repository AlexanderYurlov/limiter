package com.alex.limiter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.limiter.service.LimitStorageService;

@RestController
public class LimiterController {

    public static final String BASE_PATH = "";

    private final LimitStorageService limitStorageService;

    public LimiterController(LimitStorageService limitStorageService) {
        this.limitStorageService = limitStorageService;
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<?> method() {

        if (!limitStorageService.isAccessible()) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return ResponseEntity.ok().build();
    }
}
