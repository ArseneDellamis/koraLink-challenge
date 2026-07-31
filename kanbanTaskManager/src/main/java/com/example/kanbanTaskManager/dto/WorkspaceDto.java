package com.example.kanbanTaskManager.dto;

import com.example.kanbanTaskManager.enitiy.enums.WorkspaceRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class WorkspaceDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Workspace name is required")
        private String name;

        private String description;
    }

    @Data
    public static class AddMemberRequest {
        @NotNull(message = "User ID is required")
        private UUID userId;

        @NotNull(message = "Role is required")
        private WorkspaceRole role;
    }

    @Data
    @Builder
    public static class Response {
        private UUID id;
        private String name;
        private String description;
        private UUID ownerId;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    public static class MemberResponse {
        private UUID memberId;
        private UUID userId;
        private String fullName;
        private String email;
        private WorkspaceRole role;
        private LocalDateTime joinedAt;
    }
}
