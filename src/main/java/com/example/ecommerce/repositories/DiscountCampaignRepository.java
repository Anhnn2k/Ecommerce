package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.DiscountCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountCampaignRepository extends JpaRepository<DiscountCampaign, Long> {
}
