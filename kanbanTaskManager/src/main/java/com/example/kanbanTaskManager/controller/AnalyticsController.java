package com.example.kanbanTaskManager.controller;


import com.example.kanbanTaskManager.dto.DashboardStatsDto;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces/{workspaceId}/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<DashboardStatsDto> getWorkspaceAnalytics(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(analyticsService.getWorkspaceStats(workspaceId, currentUser));
    }
}
