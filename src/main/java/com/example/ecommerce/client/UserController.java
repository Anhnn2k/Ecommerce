package com.example.ecommerce.client;

import com.example.ecommerce.entities.Cart;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.CartDetailService;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("home/client/profile/{id}")
    public String profile(@PathVariable("id") Long id, Model model) {
        User user = this.userService.getById(id);
        Long idCart = cartService.getByUserId(user.getId()).getId();
        model.addAttribute("user", user);
        model.addAttribute("id", String.valueOf(idCart));
        model.addAttribute("id1", String.valueOf(user.getId()));
        model.addAttribute("showAcount", false);
        model.addAttribute("showCart", true);
        model.addAttribute("showAdmin", false);
        model.addAttribute("imageUrl", user.getImageUrl());
        model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
        model.addAttribute("carts", this.cartDetailService.getCartDetailByCartId(idCart));
        model.addAttribute("cartQuantity", this.cartDetailService.getCartQuantity(idCart));
        model.addAttribute("totalPrice", this.cartDetailService.getTotalPrice(idCart));
        return "client/profile/index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "client/login/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               RedirectAttributes redirectAttributes) {
        if (this.userService.findByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("message", "email account already exists");
            return "redirect:/register";
        } else {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("message", "Please check your email to activate your account.");
            return "redirect:/register";
        }
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        User user = this.userService.findByActivationCode(code);
        if (user != null) {
            user.setStatus(true);
            user.setActivationCode(null);
            userService.saveUser(user);
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setStatus(true);
            cartService.createCart(cart);

            redirectAttributes.addFlashAttribute("message", "Account activated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid activation code.");
        }

        return "redirect:/login";
    }

    @PostMapping("/home/client/update-profile")
    public String updateAcount(@ModelAttribute("user") User user) {
        this.userService.updateUser(user);
        String url = "redirect:/home/client/profile/" + user.getId();
        return url;
    }
}
