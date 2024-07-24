package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Slides;
import com.example.ecommerce.repositories.SlideRepository;
import com.example.ecommerce.services.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlideServiceImpl implements SlideService {
    @Autowired
    private SlideRepository slideRepository;

    @Override
    public List<Slides> getAll() {
        return this.slideRepository.getBySortSlide();
    }

    @Override
    public Slides getById(Long id) {
        return this.slideRepository.findById(id).get();
    }

    @Override
    public Boolean create(Slides slides) {
        try {
            this.slideRepository.save(slides);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(Slides slides) {
        try {
            this.slideRepository.save(slides);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.slideRepository.delete(this.slideRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean updateSlideOrder(List<Long> order) {
        try {
            for (int i = 0; i < order.size(); i++) {
                Long slideId = order.get(i);
                slideRepository.updateSlideOrder(slideId, i);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
