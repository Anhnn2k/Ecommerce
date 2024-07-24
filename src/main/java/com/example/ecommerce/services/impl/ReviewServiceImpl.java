package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Review;
import com.example.ecommerce.repositories.ReviewRepository;
import com.example.ecommerce.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getAll() {
        return this.reviewRepository.findAll();
    }

    @Override
    public Review getById(Long id) {
        return this.reviewRepository.findById(id).get();
    }

    @Override
    public Boolean create(Review review) {
        try {
            this.reviewRepository.save(review);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(Review review) {
        try {
            this.reviewRepository.save(review);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.reviewRepository.delete(this.reviewRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Review> findAllByUserId(Long id) {
        return this.reviewRepository.findAllByUserId(id);
    }
}
