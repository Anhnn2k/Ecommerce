package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    @Query("select p from Product p where p.category.id=?1 and p.id != ?2")
    List<Product> getByCategoryId(Long id1, Long id2);

    @Query("select p from Product  p where p.name like %:search% or p.category.name like %:search%")
    List<Product> getBySearch(@Param("search") String search);

    @Query("select p from Product p where p.category.id=?1")
    List<Product> getByCategoryId(Long id);

}
