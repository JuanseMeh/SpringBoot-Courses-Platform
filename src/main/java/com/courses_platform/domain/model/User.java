package com.courses_platform.domain.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private Set<Workspace> ownedWorkspaces = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkspaceMember> workspaceMemberships = new HashSet<>();

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Workspace> getOwnedWorkspaces() {
        return ownedWorkspaces;
    }

    public void setOwnedWorkspaces(Set<Workspace> ownedWorkspaces) {
        this.ownedWorkspaces = ownedWorkspaces;
    }

    public Set<WorkspaceMember> getWorkspaceMemberships() {
        return workspaceMemberships;
    }

    public void setWorkspaceMemberships(Set<WorkspaceMember> workspaceMemberships) {
        this.workspaceMemberships = workspaceMemberships;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
