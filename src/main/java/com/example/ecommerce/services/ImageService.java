package com.example.ecommerce.services;

import com.example.ecommerce.entities.Image;

import java.util.List;

public interface ImageService {
    List<Image> getAll();

    Image getById(Long id);

    Boolean create(Image image);

    Boolean update(Image image);

    Boolean delete(Long id);

    List<Image> getImageByProductId( Long id);
}
