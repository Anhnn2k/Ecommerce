package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Color;
import com.example.ecommerce.repositories.ColorRepository;
import com.example.ecommerce.services.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<Color> getAll() {
        return this.colorRepository.findAll();
    }

    @Override
    public List<Color> findAllById(List<Long> id) {
        List<Color> colors = new ArrayList<>();
        for (Long i : id) {
            colors.add(this.colorRepository.findById(i).get());
        }
        return colors;
    }

    @Override
    public Color getById(Long id) {
        return this.colorRepository.findById(id).get();
    }
}
