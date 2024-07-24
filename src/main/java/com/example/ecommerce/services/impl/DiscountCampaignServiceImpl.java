package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.DiscountCampaign;
import com.example.ecommerce.repositories.DiscountCampaignRepository;
import com.example.ecommerce.services.DiscountCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiscountCampaignServiceImpl implements DiscountCampaignService {
    @Autowired
    private DiscountCampaignRepository discountCampaignRepository;
    private Date createdAt;

    @Override
    public List<DiscountCampaign> getAll() {
        return this.discountCampaignRepository.findAll();
    }

    @Override
    public DiscountCampaign getById(Long id) {
        return this.discountCampaignRepository.findById(id).get();
    }

    @Override
    public Boolean create(DiscountCampaign discountCampaign) {
        try {
            this.discountCampaignRepository.save(discountCampaign);
            createdAt = discountCampaign.getCreatedAt();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(DiscountCampaign discountCampaign) {
        try {
            discountCampaign.setCreatedAt(createdAt);
            this.discountCampaignRepository.save(discountCampaign);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.discountCampaignRepository.delete(this.discountCampaignRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
