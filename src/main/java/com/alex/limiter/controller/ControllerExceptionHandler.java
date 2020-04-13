package com.alex.limiter.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice({"com.alex.limiter.controller"})
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final ResponseEntity<?> RESPONSE_CALL_LIMIT_EXCEEDED_ERROR = ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("Call Limit exceeded", e);
        return RESPONSE_CALL_LIMIT_EXCEEDED_ERROR;
    }
}