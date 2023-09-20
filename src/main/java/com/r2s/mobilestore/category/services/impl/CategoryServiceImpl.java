package com.r2s.mobilestore.category.services.impl;

import com.r2s.mobilestore.category.dto.CategoryDTO;
import com.r2s.mobilestore.category.models.Category;
import com.r2s.mobilestore.category.repositories.CategoryRepository;
import com.r2s.mobilestore.category.services.CategoryService;
import com.r2s.mobilestore.exception.DuplicatedCategoryNameException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper; // Tiêm ModelMapper

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        // Sử dụng ModelMapper để ánh xạ từ Entity sang DTO
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return modelMapper.map(category, CategoryDTO.class);
        } else {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (isCategoryNameExists(categoryDTO.getName())) {
            throw new DuplicatedCategoryNameException("Duplicated category name: " + categoryDTO.getName());
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            modelMapper.map(categoryDTO, existingCategory);
            existingCategory = categoryRepository.save(existingCategory);
            return modelMapper.map(existingCategory, CategoryDTO.class);
        } else {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return modelMapper.map(optionalCategory, CategoryDTO.class);
        } else {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }

    }

    @Override
    public boolean isCategoryNameExists(String categoryName) {
        return categoryRepository.existsByName(categoryName);
    }
}

