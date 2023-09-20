package com.r2s.mobilestore.category.controllers;

import com.r2s.mobilestore.category.dto.CategoryDTO;
import com.r2s.mobilestore.category.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        // Gọi phương thức từ Service để lấy danh sách danh mục và trả về cho client
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        // Gọi phương thức từ Service để lấy danh mục theo ID và trả về cho client
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        if (categoryService.isCategoryNameExists(categoryDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicated category name: " + categoryDTO.getName());
        }

        // Nếu không trùng lặp, thêm danh mục mới
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        // Gọi phương thức từ Service để cập nhật danh mục theo ID và trả về danh mục đã cập nhật cho client
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        // Gọi phương thức từ Service để xóa danh mục theo ID
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
