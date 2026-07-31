package com.example.kanbanTaskManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BoardDto {

    @Data
    public static class CreateBoardRequest {
        @NotBlank(message = "Board title is required")
        private String title;

        private String description;
    }

    @Data
    @Builder
    public static class BoardResponse {
        private UUID id;
        private String title;
        private String description;
        private UUID workspaceId;
        private List<ColumnDto.ColumnResponse> columns;
        private LocalDateTime createdAt;
    }
}