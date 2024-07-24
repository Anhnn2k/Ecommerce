package com.example.ecommerce.services.impl;

import com.example.ecommerce.dto.CartItemUpdateRequest;
import com.example.ecommerce.entities.Cart;
import com.example.ecommerce.entities.CartDetail;
import com.example.ecommerce.entities.ProductDetails;
import com.example.ecommerce.repositories.CartDetailRepository;
import com.example.ecommerce.repositories.ProductDetaisRepository;
import com.example.ecommerce.services.CartDetailService;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartDetailServiceImpl implements CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private ProductDetailsService productDetailsService;

    @Override
    public List<CartDetail> getAll() {
        return this.cartDetailRepository.findAll();
    }

    @Override
    public CartDetail getById(Long id) {
        return this.cartDetailRepository.findById(id).get();
    }

    @Override
    public Boolean createCartDetail(CartDetail cartDetail, Long id) {
        boolean check = false;
        try {
            for (CartDetail cartDetail1 : this.cartDetailRepository.getCartDetailByCartId(id)) {
                if (cartDetail.getProductDetails().getId() == cartDetail1.getProductDetails().getId()) {
                    cartDetail1.setQuantity(cartDetail1.getQuantity() + cartDetail.getQuantity());
                    this.cartDetailRepository.save(cartDetail1);
                    check = true;
                    break;
                }
            }
            if (check == false) {
                this.cartDetailRepository.save(cartDetail);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateCartDetail(List<CartItemUpdateRequest> cartItems, Long idCart) {
        try {
            for (CartItemUpdateRequest item : cartItems) {
                CartDetail cartDetail = this.cartDetailRepository.getByProductDetailId(item.getProductDetailId(), idCart);
                ProductDetails productDetails = this.productDetailsService.getById(item.getProductDetailId());
                if (cartDetail != null) {
                    int a = item.getQuantity() - cartDetail.getQuantity();
                    cartDetail.setQuantity(item.getQuantity());
                    productDetails.setQuantity(productDetails.getQuantity() - a);
                    this.productDetailsService.update(productDetails);
                    this.cartDetailRepository.save(cartDetail);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteCartDetail(Long id) {
        try {
            CartDetail cartDetail = this.cartDetailRepository.findById(id).get();
            this.cartDetailRepository.delete(cartDetail);
            cartDetail.getProductDetails().setQuantity(cartDetail.getProductDetails().getQuantity() + cartDetail.getQuantity());
            this.productDetailsService.update(cartDetail.getProductDetails());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer getCartQuantity(Long id) {
        return this.cartDetailRepository.getCartQuantity(id);
    }

    @Override
    public Double getTotalPrice(Long id) {
        return this.cartDetailRepository.getTotalPrice(id);
    }

    @Override
    public List<CartDetail> getCartDetailByCartId(Long id) {
        return this.cartDetailRepository.getCartDetailByCartId(id);
    }

}
