package com.example.kanbanTaskManager.repository;

import com.example.kanbanTaskManager.enitiy.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ColumnRepository extends JpaRepository<ColumnEntity, UUID> {
    List<ColumnEntity> findByBoardIdOrderByPositionAsc(UUID boardId);
}
