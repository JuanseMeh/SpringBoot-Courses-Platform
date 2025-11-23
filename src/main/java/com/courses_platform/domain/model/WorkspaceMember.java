package com.courses_platform.domain.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "workspace_member")
public class WorkspaceMember {

    @EmbeddedId
    private WorkspaceMemberId id;

    @ManyToOne(optional = false)
    @MapsId("workspaceId")
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    public WorkspaceMember() {
    }

    public static WorkspaceMember create(User user, Workspace workspace) {
        WorkspaceMember member = new WorkspaceMember();
        member.setUser(user);
        member.setWorkspace(workspace);
        member.setId(new WorkspaceMemberId(workspace.getId(), user.getId()));
        return member;
    }

    public WorkspaceMemberId getId() {
        return id;
    }

    public void setId(WorkspaceMemberId id) {
        this.id = id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkspaceMember)) return false;
        WorkspaceMember that = (WorkspaceMember) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Embeddable
    public static class WorkspaceMemberId implements Serializable {

        private static final long serialVersionUID = 1L;

        @Column(name = "workspace_id")
        private Long workspaceId;

        @Column(name = "user_id")
        private Long userId;

        public WorkspaceMemberId() {
        }

        public WorkspaceMemberId(Long workspaceId, Long userId) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WorkspaceMemberId)) return false;
            WorkspaceMemberId that = (WorkspaceMemberId) o;
            return Objects.equals(getWorkspaceId(), that.getWorkspaceId()) &&
                    Objects.equals(getUserId(), that.getUserId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getWorkspaceId(), getUserId());
        }
    }
}
