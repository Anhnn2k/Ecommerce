package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        return this.productRepository.findAll(pageable);
    }

    @Override
    public Product getById(Long id) {
        return this.productRepository.findById(id).get();
    }

    @Override
    public Boolean create(Product product) {
        try {
            this.productRepository.save(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(Product product) {
        try {
            this.productRepository.save(product);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.productRepository.delete(this.productRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> getByCategoryId(Long id1, Long id2) {
        return this.productRepository.getByCategoryId(id1, id2);
    }

    @Override
    public Double getPriceDiscount(Product product, Double discountPercentage) {
        Double newPrice = product.getPrice() * (1 - discountPercentage / 100);
        return newPrice;
    }

    @Override
    public List<Product> getBySearch(String search) {
        return this.productRepository.getBySearch(search);
    }

    @Override
    public List<Product> getByCategoryId(Long id) {
        return this.productRepository.getByCategoryId(id);
    }
}
