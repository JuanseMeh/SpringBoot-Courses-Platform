package com.courses_platform.controller;

import com.courses_platform.dto.EnrollmentDTO;
import com.courses_platform.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentDTO created = enrollmentService.createEnrollment(enrollmentDTO.getUserId(), enrollmentDTO.getCourseId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/users/{userId}/courses/{courseId}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentByUserAndCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        EnrollmentDTO enrollmentDTO = enrollmentService.getEnrollmentByUserAndCourse(userId, courseId);
        return ResponseEntity.ok(enrollmentDTO);
    }

    @DeleteMapping("/users/{userId}/courses/{courseId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long userId, @PathVariable Long courseId) {
        enrollmentService.deleteEnrollment(userId, courseId);
        return ResponseEntity.noContent().build();
    }
}
