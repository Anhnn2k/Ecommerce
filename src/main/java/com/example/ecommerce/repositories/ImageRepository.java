package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select i from Image i where i.product.id=?1")
    List<Image> getImageByProductId(Long id);
}
