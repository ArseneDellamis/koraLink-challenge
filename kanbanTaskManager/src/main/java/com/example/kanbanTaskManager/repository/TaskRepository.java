package com.example.kanbanTaskManager.repository;

import com.example.kanbanTaskManager.enitiy.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByColumnIdOrderByPositionAsc(UUID columnId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.column.board.workspace.id = :workspaceId")
    long countByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT MAX(t.position) FROM Task t WHERE t.column.id = :columnId")
    Integer findMaxPositionByColumnId(@Param("columnId") UUID columnId);
}
