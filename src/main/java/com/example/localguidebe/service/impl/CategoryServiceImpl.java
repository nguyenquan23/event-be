package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.CategoryToCategoryDtoConverter;
import com.example.localguidebe.dto.CategoryDTO;
import com.example.localguidebe.entity.Category;
import com.example.localguidebe.repository.CategoryRepository;
import com.example.localguidebe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryToCategoryDtoConverter categoryToCategoryDtoConverter;
    @Autowired
    public void setCategoryToCategoryDtoConverter(CategoryToCategoryDtoConverter categoryToCategoryDtoConverter){
        this.categoryToCategoryDtoConverter = categoryToCategoryDtoConverter;
    }
    private CategoryRepository categoryRepository;
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    @Override
    public List<CategoryDTO> getCategories() {
      return   categoryRepository.findAll().stream().map( categoryToCategoryDtoConverter::convertCategory).collect(Collectors.toList());

    }
}
