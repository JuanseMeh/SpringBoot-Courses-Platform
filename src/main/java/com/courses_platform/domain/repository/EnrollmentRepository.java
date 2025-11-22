package com.courses_platform.domain.repository;

import com.courses_platform.domain.model.Enrollment;
import com.courses_platform.domain.model.Enrollment.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
}
