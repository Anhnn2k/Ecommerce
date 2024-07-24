package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Brand;
import com.example.ecommerce.repositories.BrandRepository;
import com.example.ecommerce.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAll() {
        return this.brandRepository.findAll();
    }

    @Override
    public Brand getById(Long id) {
        return this.brandRepository.findById(id).get();
    }
}
