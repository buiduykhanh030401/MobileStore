package com.r2s.mobilestore.category.services;

import com.r2s.mobilestore.category.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long id);
    boolean isCategoryNameExists(String categoryName);
}
