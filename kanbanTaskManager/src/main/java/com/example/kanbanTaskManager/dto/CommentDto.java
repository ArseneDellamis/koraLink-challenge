package com.example.kanbanTaskManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentDto {

    @Data
    public static class CreateCommentRequest {
        @NotBlank(message = "Comment text cannot be empty")
        private String content;
    }

    @Data
    @Builder
    public static class CommentResponse {
        private UUID id;
        private String content;
        private UUID taskId;
        private UUID authorId;
        private String authorName;
        private LocalDateTime createdAt;
    }
}
