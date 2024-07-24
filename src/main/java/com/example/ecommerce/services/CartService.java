package com.example.ecommerce.services;

import com.example.ecommerce.entities.Cart;
import com.example.ecommerce.entities.User;

import java.util.List;

public interface CartService {
    List<Cart> getAll();

    Cart getById(Long id);

    Boolean createCart(Cart cart);

    Boolean updateCart(Cart cart);

    Boolean deleteCart(Long id);

    Cart getByUserId(Long id);

    User getUserByCartId(Long id);
}
