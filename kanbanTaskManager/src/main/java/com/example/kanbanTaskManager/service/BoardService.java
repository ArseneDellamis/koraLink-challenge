package com.example.kanbanTaskManager.service;

import com.example.kanbanTaskManager.dto.BoardDto;
import com.example.kanbanTaskManager.dto.ColumnDto;
import com.example.kanbanTaskManager.enitiy.Board;
import com.example.kanbanTaskManager.enitiy.ColumnEntity;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.enitiy.Workspace;
import com.example.kanbanTaskManager.repository.BoardRepository;
import com.example.kanbanTaskManager.repository.ColumnRepository;
import com.example.kanbanTaskManager.repository.WorkspaceMemberRepository;
import com.example.kanbanTaskManager.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional
    public BoardDto.BoardResponse createBoard(UUID workspaceId, BoardDto.CreateBoardRequest request, User currentUser) {
        validateWorkspaceMembership(workspaceId, currentUser.getId());

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found"));

        Board board = Board.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .workspace(workspace)
                .build();

        board = boardRepository.save(board);

        // Auto-create standard Kanban default columns: To Do (0), In Progress (1), Done (2)
        List<ColumnEntity> defaultColumns = Arrays.asList(
                ColumnEntity.builder().name("To Do").position(0).board(board).build(),
                ColumnEntity.builder().name("In Progress").position(1).board(board).build(),
                ColumnEntity.builder().name("Done").position(2).board(board).build()
        );

        columnRepository.saveAll(defaultColumns);
        board.setColumns(defaultColumns);

        return mapToBoardResponse(board);
    }

    @Transactional(readOnly = true)
    public List<BoardDto.BoardResponse> getWorkspaceBoards(UUID workspaceId, User currentUser) {
        validateWorkspaceMembership(workspaceId, currentUser.getId());

        return boardRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(this::mapToBoardResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardDto.BoardResponse getBoardById(UUID boardId, User currentUser) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        validateWorkspaceMembership(board.getWorkspace().getId(), currentUser.getId());

        return mapToBoardResponse(board);
    }

    @Transactional
    public ColumnDto.ColumnResponse addColumnToBoard(UUID boardId, ColumnDto.CreateColumnRequest request, User currentUser) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        validateWorkspaceMembership(board.getWorkspace().getId(), currentUser.getId());

        ColumnEntity column = ColumnEntity.builder()
                .name(request.getName())
                .position(request.getPosition())
                .board(board)
                .build();

        column = columnRepository.save(column);

        return mapToColumnResponse(column);
    }

    private void validateWorkspaceMembership(UUID workspaceId, UUID userId) {
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new AccessDeniedException("Access denied: You are not a member of this workspace");
        }
    }

    private BoardDto.BoardResponse mapToBoardResponse(Board board) {
        List<ColumnDto.ColumnResponse> columnResponses = board.getColumns() != null
                ? board.getColumns().stream().map(this::mapToColumnResponse).collect(Collectors.toList())
                : List.of();

        return BoardDto.BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .description(board.getDescription())
                .workspaceId(board.getWorkspace().getId())
                .columns(columnResponses)
                .createdAt(board.getCreatedAt())
                .build();
    }

    private ColumnDto.ColumnResponse mapToColumnResponse(ColumnEntity column) {
        return ColumnDto.ColumnResponse.builder()
                .id(column.getId())
                .name(column.getName())
                .position(column.getPosition())
                .boardId(column.getBoard().getId())
                .build();
    }
}
