package com.courses_platform.service;

import com.courses_platform.domain.model.Category;
import com.courses_platform.domain.model.Course;
import com.courses_platform.domain.repository.CategoryRepository;
import com.courses_platform.domain.repository.CourseRepository;
import com.courses_platform.exception.BadRequestException;
import com.courses_platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    public Course createCourse(Course course) {
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Course title must not be null or empty");
        }
        if (course.getCategory() == null || course.getCategory().getId() == null) {
            throw new BadRequestException("Course category must be specified");
        }

        Long categoryId = course.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        course.setCategory(category);

        return courseRepository.save(course);
    }

    public List<Course> listCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course existing = getCourseById(id);

        String newTitle = updatedCourse.getTitle();
        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new BadRequestException("Course title must not be null or empty");
        }

        if (updatedCourse.getCategory() == null || updatedCourse.getCategory().getId() == null) {
            throw new BadRequestException("Course category must be specified");
        }

        Long newCategoryId = updatedCourse.getCategory().getId();
        Category newCategory = categoryRepository.findById(newCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + newCategoryId));

        existing.setTitle(newTitle);
        existing.setDescription(updatedCourse.getDescription());
        existing.setCategory(newCategory);

        return courseRepository.save(existing);
    }

    public void deleteCourse(Long id) {
        Course existing = getCourseById(id);
        courseRepository.delete(existing);
    }
}
