package com.courses_platform.controller;

import com.courses_platform.domain.model.Course;
import com.courses_platform.domain.model.User;
import com.courses_platform.dto.StudentDTO;
import com.courses_platform.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final StudentService studentService;

    public UserController(StudentService studentService) {
        this.studentService = studentService;
    }

    private StudentDTO convertToDTO(User user, Set<Long> courseIds) {
        return new StudentDTO(user.getId(), user.getName(), user.getEmail(), courseIds);
    }

    @GetMapping
    public List<StudentDTO> listStudents() {
        List<User> users = studentService.listStudents();
        return users.stream()
                .map(user -> {
                    Set<Long> courseIds = studentService.listCoursesForStudent(user.getId())
                            .stream()
                            .map(Course::getId)
                            .collect(Collectors.toSet());
                    return convertToDTO(user, courseIds);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        User user = studentService.getStudentById(id);
        Set<Long> courseIds = studentService.listCoursesForStudent(id)
                .stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(convertToDTO(user, courseIds), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        User user = new User();
        user.setName(studentDTO.getName());
        user.setEmail(studentDTO.getEmail());
        User created = studentService.createStudent(user, studentDTO.getCourseIds());
        Set<Long> courseIds = studentService.listCoursesForStudent(created.getId())
                .stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(convertToDTO(created, courseIds), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        User user = new User();
        user.setName(studentDTO.getName());
        user.setEmail(studentDTO.getEmail());
        User updated = studentService.updateStudent(id, user, studentDTO.getCourseIds());
        Set<Long> courseIds = studentService.listCoursesForStudent(updated.getId())
                .stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(convertToDTO(updated, courseIds), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/courses")
    public Set<Course> listCoursesForStudent(@PathVariable Long id) {
        return studentService.listCoursesForStudent(id);
    }
}
