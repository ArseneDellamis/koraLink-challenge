package com.example.kanbanTaskManager.controller;

import com.example.kanbanTaskManager.dto.CommentDto;
import com.example.kanbanTaskManager.dto.TaskDto;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // Create a new task inside a column
    @PostMapping("/columns/{columnId}/tasks")
    public ResponseEntity<TaskDto.TaskResponse> createTask(
            @PathVariable UUID columnId,
            @Valid @RequestBody TaskDto.CreateTaskRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(columnId, request, currentUser));
    }

    // Move task between columns or update position
    @PutMapping("/tasks/{taskId}/move")
    public ResponseEntity<TaskDto.TaskResponse> moveTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskDto.MoveTaskRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.moveTask(taskId, request, currentUser));
    }

    // Get task details by ID
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto.TaskResponse> getTaskById(
            @PathVariable UUID taskId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.getTaskById(taskId, currentUser));
    }

    // Add a comment to a task
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentDto.CommentResponse> addComment(
            @PathVariable UUID taskId,
            @Valid @RequestBody CommentDto.CreateCommentRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.addComment(taskId, request, currentUser));
    }

    // Delete a task
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID taskId,
            @AuthenticationPrincipal User currentUser) {
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
