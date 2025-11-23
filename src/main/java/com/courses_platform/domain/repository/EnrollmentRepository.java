package com.courses_platform.domain.repository;

import com.courses_platform.domain.model.Enrollment;
import com.courses_platform.domain.model.Enrollment.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByCourseId(Long courseId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    @Transactional
    void deleteAllByUserId(Long userId);
}
