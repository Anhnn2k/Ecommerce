package com.example.ecommerce.services;

import com.example.ecommerce.entities.Color;

import java.util.List;

public interface ColorService {
    List<Color> getAll();

    List<Color> findAllById(List<Long> id);
    Color getById(Long id);
}
