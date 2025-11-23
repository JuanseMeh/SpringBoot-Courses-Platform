package com.courses_platform.service;

import com.courses_platform.domain.model.User;
import com.courses_platform.domain.model.Workspace;
import com.courses_platform.domain.model.WorkspaceMember;
import com.courses_platform.domain.repository.UserRepository;
import com.courses_platform.domain.repository.WorkspaceRepository;
import com.courses_platform.domain.repository.WorkSpaceMemberRepository;
import com.courses_platform.exception.BadRequestException;
import com.courses_platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkSpaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkSpaceMemberRepository workspaceMemberRepository;

    public WorkSpaceService(WorkspaceRepository workspaceRepository,
                            UserRepository userRepository,
                            WorkSpaceMemberRepository workspaceMemberRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    @Transactional
    public Workspace createWorkspace(String name, Long ownerId) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Workspace name must not be null or empty");
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner user not found with id " + ownerId));

        Workspace workspace = new Workspace(name, owner);
        workspace = workspaceRepository.save(workspace);

        WorkspaceMember ownerMember = WorkspaceMember.create(owner, workspace);
        workspaceMemberRepository.save(ownerMember);

        return workspace;
    }

    public List<Workspace> listWorkspaces() {
        return workspaceRepository.findAll();
    }

    public Workspace getWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with id " + id));
    }

    @Transactional
    public WorkspaceMember addMember(Long workspaceId, Long userId) {
        Workspace workspace = getWorkspaceById(workspaceId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        boolean alreadyMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, userId);
        if (alreadyMember) {
            throw new BadRequestException("User is already a member of the workspace");
        }

        WorkspaceMember member = WorkspaceMember.create(user, workspace);
        return workspaceMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(Long workspaceId, Long userId) {
        Workspace workspace = getWorkspaceById(workspaceId);

        if (workspace.getOwner().getId().equals(userId)) {
            throw new BadRequestException("Cannot remove owner from workspace members");
        }

        WorkspaceMember.WorkspaceMemberId memberId = new WorkspaceMember.WorkspaceMemberId(workspaceId, userId);
        if (!workspaceMemberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("User is not a member of the workspace");
        }

        workspaceMemberRepository.deleteById(memberId);
    }

    @Transactional
    public void transferOwnership(Long workspaceId, Long newOwnerId) {
        Workspace workspace = getWorkspaceById(workspaceId);

        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, newOwnerId)) {
            throw new BadRequestException("New owner must be an existing member of the workspace");
        }

        User newOwner = userRepository.findById(newOwnerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + newOwnerId));

        workspace.setOwner(newOwner);
        workspaceRepository.save(workspace);
    }
}
