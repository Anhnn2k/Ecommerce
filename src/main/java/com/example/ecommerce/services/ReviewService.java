package com.example.ecommerce.services;

import com.example.ecommerce.entities.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getAll();

    Review getById(Long id);

    Boolean create(Review review);

    Boolean update(Review review);

    Boolean delete(Long id);

    List<Review> findAllByUserId(Long id);
}
