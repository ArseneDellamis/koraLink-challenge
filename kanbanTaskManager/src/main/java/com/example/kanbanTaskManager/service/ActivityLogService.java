package com.example.kanbanTaskManager.service;

import com.example.kanbanTaskManager.dto.ActivityLogDto;
import com.example.kanbanTaskManager.enitiy.ActivityLog;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.repository.ActivityLogRepository;
import com.example.kanbanTaskManager.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional(readOnly = true)
    public List<ActivityLogDto.Response> getWorkspaceLogs(UUID workspaceId, int page, int size, User currentUser) {
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, currentUser.getId())) {
            throw new AccessDeniedException("Access denied: Not a workspace member");
        }

        Page<ActivityLog> logs = activityLogRepository.findByWorkspaceIdOrderByCreatedAtDesc(
                workspaceId, PageRequest.of(page, size));

        return logs.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ActivityLogDto.Response mapToResponse(ActivityLog log) {
        return ActivityLogDto.Response.builder()
                .id(log.getId())
                .action(log.getAction())
                .details(log.getDetails())
                .actorId(log.getActor().getId())
                .actorName(log.getActor().getFull_name())
                .taskId(log.getTask() != null ? log.getTask().getId() : null)
                .createdAt(log.getCreatedAt())
                .build();
    }
}
