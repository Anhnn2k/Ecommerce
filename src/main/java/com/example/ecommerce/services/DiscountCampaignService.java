package com.example.ecommerce.services;

import com.example.ecommerce.entities.DiscountCampaign;

import java.util.List;

public interface DiscountCampaignService {
    List<DiscountCampaign > getAll();

    DiscountCampaign getById(Long id);

    Boolean create(DiscountCampaign discountCampaign);

    Boolean update(DiscountCampaign discountCampaign);
    Boolean delete(Long id);
}
