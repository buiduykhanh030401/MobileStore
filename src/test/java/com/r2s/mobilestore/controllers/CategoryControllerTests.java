package com.r2s.mobilestore.controllers;

import com.r2s.mobilestore.category.repositories.CategoryRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.category.dto.CategoryDTO;
import com.r2s.mobilestore.category.models.Category;

import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void tearDown() {
        // Xóa tất cả category sau mỗi bài kiểm tra để đảm bảo sự tách biệt giữa các kiểm tra
        categoryRepository.deleteAll();
    }

    @Test
    public void testCreateCategory() throws Exception {
        // Tạo một category DTO
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");

        // Chuyển categoryDTO thành JSON
        String categoryJson = objectMapper.writeValueAsString(categoryDTO);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isCreated());

        // Kiểm tra xem category đã được tạo thành công bằng cách kiểm tra trong cơ sở dữ liệu
        Optional<Category> savedCategory = categoryRepository.findByName("Test Category");
        assertNotNull(savedCategory);
    }

    @Test
    public void testGetCategoryById() throws Exception {
        // Tạo một category và lưu vào cơ sở dữ liệu
        Category category = new Category();
        category.setName("Test Category");
        categoryRepository.save(category);

        mockMvc.perform(get("/categories/{id}", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        // Tạo một category và lưu vào cơ sở dữ liệu
        Category category = new Category();
        category.setName("Test Category");
        categoryRepository.save(category);

        // Chuyển category thành JSON và thay đổi tên
        category.setName("Updated Category");
        String categoryJson = objectMapper.writeValueAsString(category);

        mockMvc.perform(put("/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk());

        // Kiểm tra xem category đã được cập nhật thành công bằng cách kiểm tra trong cơ sở dữ liệu
        Category updatedCategory = categoryRepository.findById(category.getId()).orElse(null);
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        // Tạo một category và lưu vào cơ sở dữ liệu
        Category category = new Category();
        category.setName("Test Category");
        categoryRepository.save(category);

        mockMvc.perform(delete("/categories/{id}", category.getId()))
                .andExpect(status().isOk());

        // Kiểm tra xem category đã bị xóa bằng cách kiểm tra trong cơ sở dữ liệu
        Category deletedCategory = categoryRepository.findById(category.getId()).orElse(null);
        assertNull(deletedCategory);
    }
}

