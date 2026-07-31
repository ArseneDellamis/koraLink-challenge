package com.example.kanbanTaskManager.dto;

import com.example.kanbanTaskManager.enitiy.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TaskDto {

    @Data
    public static class CreateTaskRequest {
        @NotBlank(message = "Task title is required")
        private String title;

        private String description;
        private Priority priority;
        private LocalDate dueDate;
        private UUID assigneeId;
    }

    @Data
    public static class MoveTaskRequest {
        @NotNull(message = "Target column ID is required")
        private UUID targetColumnId;

        @NotNull(message = "New position index is required")
        private Integer targetPosition;
    }

    @Data
    @Builder
    public static class TaskResponse {
        private UUID id;
        private String title;
        private String description;
        private Integer position;
        private Priority priority;
        private LocalDate dueDate;
        private UUID columnId;
        private UUID assigneeId;
        private String assigneeName;
        private UUID creatorId;
        private String creatorName;
        private List<CommentDto.CommentResponse> comments;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
