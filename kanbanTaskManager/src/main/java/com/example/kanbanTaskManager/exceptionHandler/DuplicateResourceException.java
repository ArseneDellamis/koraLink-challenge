package com.example.kanbanTaskManager.exceptionHandler;

public class DuplicateResourceException extends ApiException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
