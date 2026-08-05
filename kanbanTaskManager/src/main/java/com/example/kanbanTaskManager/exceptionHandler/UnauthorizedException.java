package com.example.kanbanTaskManager.exceptionHandler;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
