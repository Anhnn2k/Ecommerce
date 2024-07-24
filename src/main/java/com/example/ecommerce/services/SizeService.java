package com.example.ecommerce.services;

import com.example.ecommerce.entities.Size;

import java.util.List;

public interface SizeService {
    List<Size> getAll();

    Size getById(Long id);

    List<Size> getAllById(List<Long> id);
}
