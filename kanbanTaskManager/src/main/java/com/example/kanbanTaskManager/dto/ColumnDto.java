package com.example.kanbanTaskManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

public class ColumnDto {

    @Data
    public static class CreateColumnRequest {
        @NotBlank(message = "Column name is required")
        private String name;

        @NotNull(message = "Position is required")
        private Integer position;
    }

    @Data
    @Builder
    public static class ColumnResponse {
        private UUID id;
        private String name;
        private Integer position;
        private UUID boardId;
    }
}
