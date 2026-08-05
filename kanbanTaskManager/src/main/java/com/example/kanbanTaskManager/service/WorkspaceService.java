package com.example.kanbanTaskManager.service;


import com.example.kanbanTaskManager.dto.WorkspaceDto;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.enitiy.Workspace;
import com.example.kanbanTaskManager.enitiy.WorkspaceMember;
import com.example.kanbanTaskManager.enitiy.enums.WorkspaceRole;
import com.example.kanbanTaskManager.exceptionHandler.ResourceNotFoundException;
import com.example.kanbanTaskManager.repository.UserRepository;
import com.example.kanbanTaskManager.repository.WorkspaceMemberRepository;
import com.example.kanbanTaskManager.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkspaceDto.Response createWorkspace(WorkspaceDto.CreateRequest request, User currentUser) {
        Workspace workspace = Workspace.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(currentUser)
                .build();

        workspace = workspaceRepository.save(workspace);

        // Automatically add owner as ADMIN member
        WorkspaceMember ownerMember = WorkspaceMember.builder()
                .workspace(workspace)
                .user(currentUser)
                .role(WorkspaceRole.ADMIN)
                .build();

        workspaceMemberRepository.save(ownerMember);

        return mapToResponse(workspace);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceDto.Response> getUserWorkspaces(User currentUser) {
        return workspaceMemberRepository.findByUserId(currentUser.getId())
                .stream()
                .map(WorkspaceMember::getWorkspace)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkspaceDto.MemberResponse addMember(UUID workspaceId, WorkspaceDto.AddMemberRequest request, User currentUser) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        validateAdminAccess(workspaceId, currentUser.getId());

        User memberToAdd = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User to add not found"));

        if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, memberToAdd.getId())) {
            throw new IllegalArgumentException("User is already a member of this workspace");
        }

        WorkspaceMember member = WorkspaceMember.builder()
                .workspace(workspace)
                .user(memberToAdd)
                .role(request.getRole())
                .build();

        member = workspaceMemberRepository.save(member);

        return mapToMemberResponse(member);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceDto.MemberResponse> getWorkspaceMembers(UUID workspaceId, User currentUser) {
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, currentUser.getId())) {
            throw new AccessDeniedException("You are not a member of this workspace");
        }

        return workspaceMemberRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(this::mapToMemberResponse)
                .collect(Collectors.toList());
    }

    private void validateAdminAccess(UUID workspaceId, UUID userId) {
        WorkspaceMember member = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new AccessDeniedException("Access denied: Not a member of this workspace"));

        if (member.getRole() != WorkspaceRole.ADMIN) {
            throw new AccessDeniedException("Access denied: Admin permissions required");
        }
    }

    private WorkspaceDto.Response mapToResponse(Workspace workspace) {
        return WorkspaceDto.Response.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .ownerId(workspace.getOwner().getId())
                .createdAt(workspace.getCreatedAt())
                .build();
    }

    private WorkspaceDto.MemberResponse mapToMemberResponse(WorkspaceMember member) {
        return WorkspaceDto.MemberResponse.builder()
                .memberId(member.getId())
                .userId(member.getUser().getId())
                .fullName(member.getUser().getFullName())
                .email(member.getUser().getEmail())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
