package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ProductDiscountCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDiscountCampaignRepository extends JpaRepository<ProductDiscountCampaign, Long> {
    @Query("select p from ProductDiscountCampaign p where p.discountCampaign.id=?1")
    List<ProductDiscountCampaign> getByDiscountId(Long id);

    @Query("select p from ProductDiscountCampaign p where p.product.id=?1")
    ProductDiscountCampaign getByProductId(Long id);

    @Query("select p from ProductDiscountCampaign  p where p.id=?1")
    ProductDiscountCampaign getByID(Long id);
}
