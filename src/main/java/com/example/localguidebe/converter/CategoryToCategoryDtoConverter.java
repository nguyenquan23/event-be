package com.example.localguidebe.converter;

import com.example.localguidebe.dto.CategoryDTO;
import com.example.localguidebe.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDtoConverter {

  public CategoryDTO convertCategory(Category category) {

    return new CategoryDTO(category.getId(), category.getName());
  }
}
