package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    @Query("select count(*) from CartDetail cd where cd.cart.id=?1")
    Integer getCartQuantity(Long id);

    @Query("select sum(cd.price*cd.quantity) from CartDetail cd where cd.cart.id=?1")
    Double getTotalPrice(Long id);

    @Query("select cd from CartDetail cd where cd.cart.id=?1")
    List<CartDetail> getCartDetailByCartId(Long id);

    @Query("select c from CartDetail c where c.productDetails.id=?1 and c.cart.id=?2")
    CartDetail getByProductDetailId(Long idProduct, Long idCart);
}
