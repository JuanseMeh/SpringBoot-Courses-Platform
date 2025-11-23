package com.courses_platform.service;

import com.courses_platform.domain.model.Category;
import com.courses_platform.domain.repository.CategoryRepository;
import com.courses_platform.exception.BadRequestException;
import com.courses_platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new BadRequestException("Category name must not be null or empty");
        }

        return categoryRepository.save(category);
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category existing = getCategoryById(id);

        String newName = updatedCategory.getName();
        if (newName == null || newName.trim().isEmpty()) {
            throw new BadRequestException("Category name must not be null or empty");
        }

        existing.setName(newName);

        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        Category existing = getCategoryById(id);
        categoryRepository.delete(existing);
    }
}
