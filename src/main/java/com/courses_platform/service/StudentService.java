package com.courses_platform.service;

import com.courses_platform.domain.model.Course;
import com.courses_platform.domain.model.Enrollment;
import com.courses_platform.domain.model.User;
import com.courses_platform.domain.repository.CourseRepository;
import com.courses_platform.domain.repository.EnrollmentRepository;
import com.courses_platform.domain.repository.UserRepository;
import com.courses_platform.exception.BadRequestException;
import com.courses_platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentService(UserRepository userRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<User> listStudents() {
        return userRepository.findAll();
    }

    public User getStudentById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    @Transactional
    public User createStudent(User user, Set<Long> courseIds) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new BadRequestException("Student name must not be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Student email must not be null or empty");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Student email must be unique");
        }

        User savedUser = userRepository.save(user);

        if (courseIds != null && !courseIds.isEmpty()) {
            List<Course> courses = courseRepository.findAllById(courseIds);
            if (courses.size() != courseIds.size()) {
                throw new BadRequestException("Some courses do not exist");
            }
            Set<Enrollment> enrollments = courses.stream()
                    .map(course -> new Enrollment(savedUser, course))
                    .collect(Collectors.toSet());
            enrollmentRepository.saveAll(enrollments);
        }

        return savedUser;
    }

    @Transactional
    public User updateStudent(Long id, User updatedUser, Set<Long> courseIds) {
        User existingUser = getStudentById(id);

        String newName = updatedUser.getName();
        if (newName == null || newName.trim().isEmpty()) {
            throw new BadRequestException("Student name must not be null or empty");
        }
        String newEmail = updatedUser.getEmail();
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new BadRequestException("Student email must not be null or empty");
        }
        if (!newEmail.equals(existingUser.getEmail())) {
            if (userRepository.findByEmail(newEmail).isPresent()) {
                throw new BadRequestException("Student email must be unique");
            }
            existingUser.setEmail(newEmail);
        }

        existingUser.setName(newName);

        if (courseIds != null) {
            List<Course> courses = courseRepository.findAllById(courseIds);
            if (courses.size() != courseIds.size()) {
                throw new BadRequestException("Some courses do not exist");
            }
            enrollmentRepository.deleteAllByUserId(existingUser.getId());
            Set<Enrollment> newEnrollments = courses.stream()
                    .map(course -> new Enrollment(existingUser, course))
                    .collect(Collectors.toSet());
            enrollmentRepository.saveAll(newEnrollments);
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteStudent(Long id) {
        User existingUser = getStudentById(id);
        enrollmentRepository.deleteAllByUserId(existingUser.getId());
        userRepository.delete(existingUser);
    }

    public Set<Course> listCoursesForStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(studentId);
        Set<Course> courses = new HashSet<>();
        for (Enrollment e : enrollments) {
            courses.add(e.getCourse());
        }
        return courses;
    }
}
