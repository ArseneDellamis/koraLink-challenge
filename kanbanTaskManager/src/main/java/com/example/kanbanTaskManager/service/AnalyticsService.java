package com.example.kanbanTaskManager.service;

import com.example.kanbanTaskManager.dto.DashboardStatsDto;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.repository.BoardRepository;
import com.example.kanbanTaskManager.repository.TaskRepository;
import com.example.kanbanTaskManager.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getWorkspaceStats(UUID workspaceId, User currentUser) {
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, currentUser.getId())) {
            throw new AccessDeniedException("Access denied: Not a member of this workspace");
        }

        long totalBoards = boardRepository.findByWorkspaceId(workspaceId).size();
        long totalTasks = taskRepository.countByWorkspaceId(workspaceId);
        long overdueTasks = taskRepository.countOverdueTasksByWorkspaceId(workspaceId);

        List<Object[]> columnCounts = taskRepository.countTasksByColumnForWorkspace(workspaceId);
        Map<String, Long> tasksByColumn = new HashMap<>();
        for (Object[] result : columnCounts) {
            String columnName = (String) result[0];
            Long count = (Long) result[1];
            tasksByColumn.put(columnName, count);
        }

        return DashboardStatsDto.builder()
                .totalProjects(1) // Current Workspace context
                .totalBoards(totalBoards)
                .totalTasks(totalTasks)
                .overdueTasks(overdueTasks)
                .tasksByColumn(tasksByColumn)
                .build();
    }
}
