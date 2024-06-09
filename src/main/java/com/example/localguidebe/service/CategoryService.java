package com.example.localguidebe.service;

import com.example.localguidebe.dto.CategoryDTO;
import com.example.localguidebe.entity.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    List<CategoryDTO> getCategories();
}
