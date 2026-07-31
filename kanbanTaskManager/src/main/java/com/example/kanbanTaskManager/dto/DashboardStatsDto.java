package com.example.kanbanTaskManager.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class DashboardStatsDto {
    private long totalProjects;
    private long totalBoards;
    private long totalTasks;
    private long overdueTasks;
    private Map<String, Long> tasksByColumn;
}