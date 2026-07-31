package com.example.kanbanTaskManager.controller;

import com.example.kanbanTaskManager.dto.ActivityLogDto;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces/{workspaceId}/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<List<ActivityLogDto.Response>> getWorkspaceActivityLogs(
            @PathVariable UUID workspaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(activityLogService.getWorkspaceLogs(workspaceId, page, size, currentUser));
    }
}