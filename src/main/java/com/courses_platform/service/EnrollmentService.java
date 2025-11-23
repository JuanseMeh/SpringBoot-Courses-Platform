package com.courses_platform.service;

import com.courses_platform.domain.model.Enrollment;
import com.courses_platform.domain.model.User;
import com.courses_platform.domain.model.Course;
import com.courses_platform.domain.repository.EnrollmentRepository;
import com.courses_platform.dto.EnrollmentDTO;
import com.courses_platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentService studentService, CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @Transactional
    public EnrollmentDTO createEnrollment(Long userId, Long courseId) {
        User user = studentService.getStudentById(userId);
        Course course = courseService.getCourseById(courseId); 

        if (course == null) {
            throw new ResourceNotFoundException("Course not found with id " + courseId);
        }

        Enrollment enrollment = new Enrollment(user, course);
        Enrollment saved = enrollmentRepository.save(enrollment);
        return new EnrollmentDTO(saved.getUser().getId(), saved.getCourse().getId());
    }

    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(e -> new EnrollmentDTO(e.getUser().getId(), e.getCourse().getId()))
                .collect(Collectors.toList());
    }

    public EnrollmentDTO getEnrollmentByUserAndCourse(Long userId, Long courseId) {
        return enrollmentRepository.findById(new Enrollment.EnrollmentId(userId, courseId))
                .map(e -> new EnrollmentDTO(e.getUser().getId(), e.getCourse().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for userId = " + userId + " and courseId = " + courseId));
    }

    @Transactional
    public void deleteEnrollment(Long userId, Long courseId) {
        Enrollment.EnrollmentId id = new Enrollment.EnrollmentId(userId, courseId);
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found for userId = " + userId + " and courseId = " + courseId);
        }
        enrollmentRepository.deleteById(id);
    }
}
