package com.example.kanbanTaskManager.controller;

import com.example.kanbanTaskManager.dto.BoardDto;
import com.example.kanbanTaskManager.dto.ColumnDto;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // Create a new Board within a Workspace
    @PostMapping("/workspaces/{workspaceId}/boards")
    public ResponseEntity<BoardDto.BoardResponse> createBoard(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody BoardDto.CreateBoardRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(boardService.createBoard(workspaceId, request, currentUser));
    }

    // List all Boards in a Workspace
    @GetMapping("/workspaces/{workspaceId}/boards")
    public ResponseEntity<List<BoardDto.BoardResponse>> getWorkspaceBoards(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(boardService.getWorkspaceBoards(workspaceId, currentUser));
    }

    // Get a specific Board by ID
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardDto.BoardResponse> getBoardById(
            @PathVariable UUID boardId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(boardService.getBoardById(boardId, currentUser));
    }

    // Add a custom Column to a Board
    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<ColumnDto.ColumnResponse> addColumnToBoard(
            @PathVariable UUID boardId,
            @Valid @RequestBody ColumnDto.CreateColumnRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(boardService.addColumnToBoard(boardId, request, currentUser));
    }
}
