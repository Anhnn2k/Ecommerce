package com.example.ecommerce.client;

import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.CartDetailService;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.CategoryService;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {
    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping("/home/client/cart-view/{id}")
    public String cart(Model model, @PathVariable("id") Long id) {
        model.addAttribute("showCart", false);
        model.addAttribute("showAcount", true);
        model.addAttribute("showAdmin", false);
        model.addAttribute("name", cartService.getUserByCartId(id).getFirstName() + " " + cartService.getUserByCartId(id).getLastName());
        model.addAttribute("cartQuantity", this.cartDetailService.getCartQuantity(id));
        model.addAttribute("id1", String.valueOf(cartService.getUserByCartId(id).getId()));
        model.addAttribute("carts", this.cartDetailService.getCartDetailByCartId(id));
        model.addAttribute("totalPrice", this.cartDetailService.getTotalPrice(id) != null ? this.cartDetailService.getTotalPrice(id) : 0);
        model.addAttribute("categories", this.categoryService.getAll());
        model.addAttribute("imageUrl", cartService.getUserByCartId(id).getImageUrl());
        return "client/cart/index";
    }

    @GetMapping("/home/cart-detail/remove/{id}")
    public String removeCartDetail(@PathVariable("id") Long id) {
        this.cartDetailService.deleteCartDetail(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            user = this.userService.findByEmail(oidcUser.getEmail());
        }else if (authentication.getPrincipal() instanceof OAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            user = this.userService.findByEmail(oAuth2User.getAttribute("email"));
        } else {
            throw new IllegalStateException("Unexpected user type: " + principal.getClass());
        }
        Long idCart = this.cartService.getByUserId(user.getId()).getId();
        String url = "redirect:/home/client/cart-view/" + idCart;
        return url;
    }
}
