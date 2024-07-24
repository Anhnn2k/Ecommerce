package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Size;
import com.example.ecommerce.repositories.SizeRepository;
import com.example.ecommerce.services.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> getAll() {
        return this.sizeRepository.findAll();
    }

    @Override
    public Size getById(Long id) {
        return this.sizeRepository.findById(id).get();
    }

    @Override
    public List<Size> getAllById(List<Long> id) {
        List<Size> sizes = new ArrayList<>();
        for (Long i : id) {
            sizes.add(this.sizeRepository.findById(i).get());
        }
        return sizes;
    }
}
