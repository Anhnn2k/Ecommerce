package com.example.ecommerce.services;

import com.example.ecommerce.dto.CartItemUpdateRequest;
import com.example.ecommerce.entities.CartDetail;

import java.util.List;

public interface CartDetailService {
    List<CartDetail> getAll();

    CartDetail getById(Long id);

    Boolean createCartDetail(CartDetail cartDetail, Long id);

    Boolean updateCartDetail(List<CartItemUpdateRequest> cartItems, Long idCart);
    Boolean deleteCartDetail(Long id);

    Integer getCartQuantity(Long id);

    Double getTotalPrice(Long id);

    List<CartDetail> getCartDetailByCartId(Long id);

}
