package com.courses_platform.dto;

public class WorkspaceMemberDTO {
    private Long workspaceId;
    private Long userId;

    public WorkspaceMemberDTO() {}

    public WorkspaceMemberDTO(Long workspaceId, Long userId) {
        this.workspaceId = workspaceId;
        this.userId = userId;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
