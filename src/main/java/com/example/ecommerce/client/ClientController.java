package com.example.ecommerce.client;

import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.CartDetailService;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.CategoryService;
import com.example.ecommerce.services.ProductDetailsService;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.SlideService;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home")
public class ClientController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SlideService slideService;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDetailsService productDetailsService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String index() {
        return "redirect:/home/";
    }

    @RequestMapping("/")
    public String getClient(@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                            @RequestParam(name = "size", defaultValue = "8") int size,
                            Model model) {
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
        }
        else {
            throw new IllegalStateException("Unexpected user type: " + principal.getClass());
        }

        if (user.getRole().getRoleName().equals("ADMIN")) {
            model.addAttribute("showAdmin", true);
        } else {
            model.addAttribute("showAdmin", false);
        }
        Long id = this.cartService.getByUserId(user.getId()).getId();
        model.addAttribute("id", String.valueOf(id));
        model.addAttribute("showCart", true);
        model.addAttribute("id1", String.valueOf(user.getId()));
        model.addAttribute("showAcount", true);
        model.addAttribute("carts", this.cartDetailService.getCartDetailByCartId(id));
        model.addAttribute("cartQuantity", this.cartDetailService.getCartQuantity(id));
        model.addAttribute("totalPrice", this.cartDetailService.getTotalPrice(id) != null ? this.cartDetailService.getTotalPrice(id) : 0);
        model.addAttribute("imageUrl", user.getImageUrl());
        model.addAttribute("name", (user.getFirstName() != null && user.getLastName() != null) ? user.getFirstName() + " " + user.getLastName() : "Client");

        model.addAttribute("categories", this.categoryService.getAll());
        model.addAttribute("slides", this.slideService.getAll());
        model.addAttribute("products", this.productService.findAll(pageNo, size));
        model.addAttribute("hotProducts", this.productDetailsService.getHotProductDetais());
        return "index";
    }

}
