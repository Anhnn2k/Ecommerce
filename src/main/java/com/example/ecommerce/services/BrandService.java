package com.example.ecommerce.services;

import com.example.ecommerce.entities.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAll();

    Brand getById(Long id);
}
