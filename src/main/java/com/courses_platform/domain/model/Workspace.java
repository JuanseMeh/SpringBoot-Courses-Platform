package com.courses_platform.domain.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "workspace")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkspaceMember> members = new HashSet<>();

    public Workspace() {}

    public Workspace(String name, User owner) {
        this.name = name;
        this.owner = owner;
        owner.getOwnedWorkspaces().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<WorkspaceMember> getMembers() {
        return members;
    }

    public void setMembers(Set<WorkspaceMember> members) {
        this.members = members;
    }

    public void addMember(WorkspaceMember member) {
        members.add(member);
        member.setWorkspace(this);
        member.getUser().getWorkspaceMemberships().add(member);
    }

    public void removeMember(WorkspaceMember member) {
        members.remove(member);
        member.setWorkspace(null);
        member.getUser().getWorkspaceMemberships().remove(member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workspace)) return false;
        Workspace workspace = (Workspace) o;
        return Objects.equals(getId(), workspace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
