package com.example.ecommerce.services;

import com.example.ecommerce.entities.Slides;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SlideService {
    List<Slides> getAll();

    Slides getById(Long id);

    Boolean create(Slides slides);

    Boolean update(Slides slides);

    Boolean delete(Long id);

    @Transactional
    Boolean updateSlideOrder(List<Long> order);

}
