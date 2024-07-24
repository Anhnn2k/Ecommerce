package com.example.ecommerce.services;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    Category getById(Long id);

    Boolean create(Category category);

    Boolean update(Category category);

    Boolean delete(Long id);

    Category convertToEntity(CategoryDTO categoryDTO);

    CategoryDTO convertToDTO(Category category);

    List<Category> getByStatus();
}
