package com.courses_platform.controller;

import com.courses_platform.domain.model.Workspace;
import com.courses_platform.domain.model.WorkspaceMember;
import com.courses_platform.dto.WorkspaceDTO;
import com.courses_platform.dto.WorkspaceMemberDTO;
import com.courses_platform.service.WorkSpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkSpaceService workspaceService;

    public WorkspaceController(WorkSpaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceDTO> createWorkspace(@RequestBody WorkspaceDTO workspaceDTO) {
        Workspace created = workspaceService.createWorkspace(workspaceDTO.getName(), workspaceDTO.getOwnerId());
        WorkspaceDTO createdDTO = new WorkspaceDTO(created.getId(), created.getName(),
                created.getOwner() != null ? created.getOwner().getId() : null);
        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public List<WorkspaceDTO> listWorkspaces() {
        return workspaceService.listWorkspaces()
                .stream()
                .map(ws -> new WorkspaceDTO(ws.getId(), ws.getName(),
                        ws.getOwner() != null ? ws.getOwner().getId() : null))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable Long id) {
        Workspace workspace = workspaceService.getWorkspaceById(id);
        WorkspaceDTO workspaceDTO = new WorkspaceDTO(workspace.getId(), workspace.getName(),
                workspace.getOwner() != null ? workspace.getOwner().getId() : null);
        return new ResponseEntity<>(workspaceDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<WorkspaceMemberDTO> addMember(@PathVariable Long id, @RequestBody WorkspaceMemberDTO workspaceMemberDTO) {
        WorkspaceMember member = workspaceService.addMember(id, workspaceMemberDTO.getUserId());
        WorkspaceMemberDTO memberDTO = new WorkspaceMemberDTO(member.getWorkspace().getId(), member.getUser().getId());
        return new ResponseEntity<>(memberDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long userId) {
        workspaceService.removeMember(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/owner")
    public ResponseEntity<Void> transferOwnership(@PathVariable Long id, @RequestBody WorkspaceMemberDTO workspaceMemberDTO) {
        workspaceService.transferOwnership(id, workspaceMemberDTO.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
