package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
