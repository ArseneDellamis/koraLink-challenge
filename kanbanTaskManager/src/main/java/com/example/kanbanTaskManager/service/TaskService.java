package com.example.kanbanTaskManager.service;

import com.example.kanbanTaskManager.dto.CommentDto;
import com.example.kanbanTaskManager.dto.TaskDto;
import com.example.kanbanTaskManager.enitiy.ColumnEntity;
import com.example.kanbanTaskManager.enitiy.Comment;
import com.example.kanbanTaskManager.enitiy.Task;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.enitiy.enums.Priority;
import com.example.kanbanTaskManager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional
    public TaskDto.TaskResponse createTask(UUID columnId, TaskDto.CreateTaskRequest request, User currentUser) {
        ColumnEntity column = columnRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));

        validateWorkspaceMembership(column.getBoard().getWorkspace().getId(), currentUser.getId());

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new IllegalArgumentException("Assignee user not found"));
        }

        // Calculate next position index
        Integer maxPos = taskRepository.findMaxPositionByColumnId(columnId);
        int nextPosition = (maxPos == null) ? 0 : maxPos + 1;

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .dueDate(request.getDueDate())
                .position(nextPosition)
                .column(column)
                .assignee(assignee)
                .creator(currentUser)
                .build();

        task = taskRepository.save(task);

        return mapToTaskResponse(task);
    }

    @Transactional
    public TaskDto.TaskResponse moveTask(UUID taskId, TaskDto.MoveTaskRequest request, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        validateWorkspaceMembership(task.getColumn().getBoard().getWorkspace().getId(), currentUser.getId());

        ColumnEntity targetColumn = columnRepository.findById(request.getTargetColumnId())
                .orElseThrow(() -> new IllegalArgumentException("Target column not found"));

        task.setColumn(targetColumn);
        task.setPosition(request.getTargetPosition());

        task = taskRepository.save(task);

        return mapToTaskResponse(task);
    }

    @Transactional
    public CommentDto.CommentResponse addComment(UUID taskId, CommentDto.CreateCommentRequest request, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        validateWorkspaceMembership(task.getColumn().getBoard().getWorkspace().getId(), currentUser.getId());

        Comment comment = Comment.builder()
                .content(request.getContent())
                .task(task)
                .author(currentUser)
                .build();

        comment = commentRepository.save(comment);

        return mapToCommentResponse(comment);
    }

    @Transactional(readOnly = true)
    public TaskDto.TaskResponse getTaskById(UUID taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        validateWorkspaceMembership(task.getColumn().getBoard().getWorkspace().getId(), currentUser.getId());

        return mapToTaskResponse(task);
    }

    @Transactional
    public void deleteTask(UUID taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        validateWorkspaceMembership(task.getColumn().getBoard().getWorkspace().getId(), currentUser.getId());

        taskRepository.delete(task);
    }

    private void validateWorkspaceMembership(UUID workspaceId, UUID userId) {
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new AccessDeniedException("Access denied: You are not a member of this workspace");
        }
    }

    public TaskDto.TaskResponse mapToTaskResponse(Task task) {
        List<CommentDto.CommentResponse> commentResponses = task.getComments() != null
                ? task.getComments().stream().map(this::mapToCommentResponse).collect(Collectors.toList())
                : List.of();

        return TaskDto.TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .position(task.getPosition())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .columnId(task.getColumn().getId())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .assigneeName(task.getAssignee() != null ? task.getAssignee().getFull_name() : null)
                .creatorId(task.getCreator().getId())
                .creatorName(task.getCreator().getFull_name())
                .comments(commentResponses)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    private CommentDto.CommentResponse mapToCommentResponse(Comment comment) {
        return CommentDto.CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getFull_name())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}