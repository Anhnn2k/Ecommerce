package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Color;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductDetails;
import com.example.ecommerce.entities.Size;
import com.example.ecommerce.repositories.ProductDetaisRepository;
import com.example.ecommerce.services.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
    @Autowired
    private ProductDetaisRepository productDetaisRepository;

    @Override
    public List<ProductDetails> getAll() {
        return this.productDetaisRepository.findAll();
    }

    @Override
    public ProductDetails getById(Long id) {
        return this.productDetaisRepository.findById(id).get();
    }

    @Override
    public Boolean create(ProductDetails productDetails) {
        try {
            this.productDetaisRepository.save(productDetails);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(ProductDetails productDetails) {
        try {
            this.productDetaisRepository.save(productDetails);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.productDetaisRepository.delete(this.productDetaisRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<ProductDetails> getAllProducts(int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        return this.productDetaisRepository.findAll(pageable);
    }

    @Override
    public List<ProductDetails> getByProductId(Long id) {
        return this.productDetaisRepository.getByProductId(id);
    }

    @Override
    public Page<ProductDetails> findAllByProductId(int pageNo, int size, Long id) {
        Pageable pageable = PageRequest.of(pageNo, size);
        return this.productDetaisRepository.findAllByProductId(pageable, id);
    }

    @Override
    public List<Color> getCoLorByProductId(Long id) {
        return this.productDetaisRepository.getCoLorByProductId(id);
    }

    @Override
    public List<Size> getSizeByProductId(Long id) {
        return this.productDetaisRepository.getSizeByProductId(id);
    }

    @Override
    public ProductDetails getByProductAndSizeAndColor(Long idProduct, Long idSize, Long idColor) {
        return this.productDetaisRepository.getByProductAndSizeAndColor(idProduct, idSize, idColor);
    }

    @Override
    public List<Product> getHotProductDetais() {
        return this.productDetaisRepository.getHotProductDetais();
    }

}
