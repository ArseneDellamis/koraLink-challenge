package com.example.kanbanTaskManager.repository;

import com.example.kanbanTaskManager.enitiy.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    List<Board> findByWorkspaceId(UUID workspaceId);
}
