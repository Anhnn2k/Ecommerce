package com.example.ecommerce.services;

import com.example.ecommerce.entities.ProductDiscountCampaign;

import java.util.List;

public interface ProductDiscountCampaignService {
    List<ProductDiscountCampaign> getAll();

    ProductDiscountCampaign getById(Long id);

    Boolean create(ProductDiscountCampaign productDiscountCampaign);

    Boolean update(ProductDiscountCampaign productDiscountCampaign);

    Boolean delete(Long id);
    List<ProductDiscountCampaign> getByDiscountId(Long id);
    ProductDiscountCampaign getByProductId(Long id);
}
