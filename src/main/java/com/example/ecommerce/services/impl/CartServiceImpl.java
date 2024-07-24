package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Cart;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.repositories.CartRepository;
import com.example.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;


    @Override
    public List<Cart> getAll() {
        return this.cartRepository.findAll();
    }

    @Override
    public Cart getById(Long id) {
        return this.cartRepository.findById(id).get();
    }

    @Override
    public Boolean createCart(Cart cart) {
        try {
            this.cartRepository.save(cart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateCart(Cart cart) {
        try {
            this.cartRepository.save(cart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteCart(Long id) {
        try {
            this.cartRepository.delete(this.cartRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Cart getByUserId(Long id) {
        return this.cartRepository.getByUserId(id);
    }

    @Override
    public User getUserByCartId(Long id) {
        return this.cartRepository.getUserByCartId(id);
    }
}
