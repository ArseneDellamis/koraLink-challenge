package com.example.kanbanTaskManager.repository;

import com.example.kanbanTaskManager.enitiy.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRepository
        extends JpaRepository<Workspace, UUID> {
}
