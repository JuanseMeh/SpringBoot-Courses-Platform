package com.courses_platform.domain.repository;

import com.courses_platform.domain.model.WorkspaceMember;
import com.courses_platform.domain.model.WorkspaceMember.WorkspaceMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkSpaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {
    List<WorkspaceMember> findByUserId(Long userId);
    List<WorkspaceMember> findByWorkspaceId(Long workspaceId);
    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);
}
