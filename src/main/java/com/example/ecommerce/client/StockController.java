package com.example.ecommerce.client;

import com.example.ecommerce.dto.CartItemUpdateRequest;
import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.ProductDetails;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.CartDetailService;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.ProductDetailsService;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StockController {
    @Autowired
    private ProductDetailsService productDetailsService;
    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @GetMapping("/stock")
    public ResponseEntity<Map<String, Integer>> getStock(@RequestParam("size") Long sizeId,
                                                         @RequestParam("color") Long colorId,
                                                         @RequestParam("productId") Long productId
    ) {
        ProductDetails productDetails = this.productDetailsService.getByProductAndSizeAndColor(productId, sizeId, colorId);
        int stock = 0;
        if (productDetails != null) {
            stock = productDetails.getQuantity();
        }
        Map<String, Integer> response = new HashMap<>();
        response.put("stock", stock);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stockCart")
    public ResponseEntity<Map<String, Integer>> getStockCart(@RequestParam("productDetailId") Long id) {
        ProductDetails productDetails = this.productDetailsService.getById(id);
        int stockCart = 0;
        if (productDetails != null) {
            stockCart = productDetails.getQuantity();
        }
        Map<String, Integer> response = new HashMap<>();
        response.put("stockCart", stockCart);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestBody List<CartItemUpdateRequest> cartItems) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            user = this.userService.findByEmail(oidcUser.getEmail());
        } else {
            throw new IllegalStateException("Unexpected user type: " + principal.getClass());
        }
        Long idCart = this.cartService.getByUserId(user.getId()).getId();
        try {
            this.cartDetailService.updateCartDetail(cartItems, idCart);
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("success", false));
        }
    }
}
