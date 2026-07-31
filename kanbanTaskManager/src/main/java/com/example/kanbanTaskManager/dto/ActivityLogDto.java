package com.example.kanbanTaskManager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActivityLogDto {

    @Data
    @Builder
    public static class Response {
        private UUID id;
        private String action;
        private String details;
        private UUID actorId;
        private String actorName;
        private UUID taskId;
        private LocalDateTime createdAt;
    }
}
