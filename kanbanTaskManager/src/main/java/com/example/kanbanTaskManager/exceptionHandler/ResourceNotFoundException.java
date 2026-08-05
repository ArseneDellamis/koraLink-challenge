package com.example.kanbanTaskManager.exceptionHandler;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
