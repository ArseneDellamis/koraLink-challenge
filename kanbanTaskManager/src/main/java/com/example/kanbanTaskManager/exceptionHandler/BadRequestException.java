package com.example.kanbanTaskManager.exceptionHandler;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message);
    }
}
