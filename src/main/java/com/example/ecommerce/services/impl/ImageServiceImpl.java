package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Image;
import com.example.ecommerce.repositories.ImageRepository;
import com.example.ecommerce.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Image> getAll() {
        return this.imageRepository.findAll();
    }

    @Override
    public Image getById(Long id) {
        return this.imageRepository.findById(id).get();
    }

    @Override
    public Boolean create(Image image) {
        try {
            this.imageRepository.save(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean update(Image image) {
        try {
            this.imageRepository.save(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.imageRepository.delete(this.imageRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Image> getImageByProductId(Long id) {
        return this.imageRepository.getImageByProductId(id);
    }
}
