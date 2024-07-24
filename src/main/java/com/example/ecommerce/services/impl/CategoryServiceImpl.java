package com.example.ecommerce.services.impl;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.entities.Category;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categotyRepository;
    @Autowired
    private final ObjectMapper objectMapper;

    public CategoryServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Category> getAll() {

        return this.categotyRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return this.categotyRepository.findById(id).get();
    }

    @Override
    public Boolean create(Category category) {
        try {
            this.categotyRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean update(Category category) {
        try {
            this.categotyRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.categotyRepository.delete(this.categotyRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = this.objectMapper.convertValue(categoryDTO, Category.class);
        return category;
    }

    @Override
    public CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = this.objectMapper.convertValue(category, CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public List<Category> getByStatus() {
        return this.categotyRepository.getByStatus();
    }
}
