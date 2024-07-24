package com.example.ecommerce.services;

import com.example.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    Page<Product> findAll(int pageNo, int size);
    Product getById(Long id);

    Boolean create(Product product);
    Boolean update(Product product);
    Boolean delete(Long id);

    List<Product> getByCategoryId(Long id1, Long id2);

    Double getPriceDiscount(Product product, Double discountPercentage);

    List<Product> getBySearch(String search);

    List<Product> getByCategoryId(Long id);
}
