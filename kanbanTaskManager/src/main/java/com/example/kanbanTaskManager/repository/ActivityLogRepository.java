package com.example.kanbanTaskManager.repository;

import com.example.kanbanTaskManager.enitiy.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {
    List<ActivityLog> findByWorkspaceIdOrderByCreatedAtDesc(UUID workspaceId);
    Page<ActivityLog> findByWorkspaceIdOrderByCreatedAtDesc(UUID workspaceId, Pageable pageable);
}
