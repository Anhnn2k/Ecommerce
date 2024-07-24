package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.ProductDiscountCampaign;
import com.example.ecommerce.repositories.ProductDiscountCampaignRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.services.ProductDiscountCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDiscountCampaignServiceImpl implements ProductDiscountCampaignService {
    @Autowired
    private ProductDiscountCampaignRepository pdcr;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDiscountCampaign> getAll() {
        return this.pdcr.findAll();
    }

    @Override
    public ProductDiscountCampaign getById(Long id) {
        return this.pdcr.getByID(id);
    }

    @Override
    public Boolean create(ProductDiscountCampaign productDiscountCampaign) {
        try {
            this.pdcr.save(productDiscountCampaign);
            productDiscountCampaign.getProduct().setPrice(productDiscountCampaign.getProduct().getPrice() * (1 - (productDiscountCampaign.getDiscountCampaign().getDiscountPercentage() / 100)));
            this.productRepository.save(productDiscountCampaign.getProduct());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean update(ProductDiscountCampaign productDiscountCampaign) {
        try {
            this.pdcr.save(productDiscountCampaign);
            productDiscountCampaign.getProduct().setPrice(productDiscountCampaign.getProduct().getPrice() * (1 - (productDiscountCampaign.getDiscountCampaign().getDiscountPercentage() / 100)));
            this.productRepository.save(productDiscountCampaign.getProduct());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(Long id) {
        try {
            ProductDiscountCampaign productDiscountCampaign = this.pdcr.getByID(id);
            this.pdcr.delete(productDiscountCampaign);
            productDiscountCampaign.getProduct().setPrice(productDiscountCampaign.getProduct().getPrice() / (1 - productDiscountCampaign.getDiscountCampaign().getDiscountPercentage() / 100));
            this.productRepository.save(productDiscountCampaign.getProduct());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ProductDiscountCampaign> getByDiscountId(Long id) {
        return this.pdcr.getByDiscountId(id);
    }

    @Override
    public ProductDiscountCampaign getByProductId(Long id) {
        return this.pdcr.getByProductId(id);
    }
}
