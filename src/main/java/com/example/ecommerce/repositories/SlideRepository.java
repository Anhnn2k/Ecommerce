package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Slides;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slides, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Slides s SET s.oder = :order WHERE s.id = :id")
    void updateSlideOrder(Long id, Integer order);

    @Query("select s from Slides s order by s.oder asc ")
    List<Slides> getBySortSlide();
}
