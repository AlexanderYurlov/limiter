package com.alex.limiter.service;

public class CallLimitExceededException extends Exception {

    public CallLimitExceededException(String message) {
        super(message);
    }

}
