package com.example.kanbanTaskManager.controller;

import com.example.kanbanTaskManager.dto.WorkspaceDto;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<WorkspaceDto.Response> createWorkspace(
            @Valid @RequestBody WorkspaceDto.CreateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workspaceService.createWorkspace(request, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceDto.Response>> getUserWorkspaces(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(workspaceService.getUserWorkspaces(currentUser));
    }

    @PostMapping("/{workspaceId}/members")
    public ResponseEntity<WorkspaceDto.MemberResponse> addMember(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody WorkspaceDto.AddMemberRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workspaceService.addMember(workspaceId, request, currentUser));
    }

    @GetMapping("/{workspaceId}/members")
    public ResponseEntity<List<WorkspaceDto.MemberResponse>> getWorkspaceMembers(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(workspaceService.getWorkspaceMembers(workspaceId, currentUser));
    }
}
