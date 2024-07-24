package com.example.ecommerce.admin;

import com.example.ecommerce.entities.CartDetail;
import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.Review;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.CartDetailService;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.ReviewService;
import com.example.ecommerce.services.RoleService;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private ReviewService reviewService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @GetMapping("/admin/user")
    public String getAllUser(@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                             @RequestParam(name = "size", defaultValue = "4") int size,
                             Model model) {
        this.getUser(model);
        Page<User> users = this.userService.findAll(pageNo, size);
        model.addAttribute("users", users);
        return "admin/user/index";
    }

    @GetMapping("/admin/add-user")
    public String viewAddUser(Model model) {
        this.getUser(model);
        model.addAttribute("roles", this.roleService.getAll());
        User user = new User();
        user.setGender(true);
        user.setStatus(true);
        model.addAttribute("user1", user);
        return "admin/user/add";
    }

    @PostMapping("/admin/add-user")
    public String addUser(@ModelAttribute("user1") User user,
                          Model model) {
        if (this.userService.createUser(user)) {
            return "redirect:/admin/user";
        } else {
            return "admin/user/add";
        }
    }

    @GetMapping("/admin/update-user/{id}")
    public String updateViewUser(@PathVariable("id") Long id, Model model) {
        this.getUser(model);
        model.addAttribute("roles", this.roleService.getAll());
        model.addAttribute("user1", this.userService.getById(id));
        return "admin/user/update";
    }

    @PostMapping("/admin/update-user")
    public String updateUser(@ModelAttribute("user1") User user) {

        if (this.userService.updateUser(user)) {
            return "redirect:/admin/user";
        } else {
            return "admin/user/update";
        }
    }

    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        Long idCart = cartService.getByUserId(id).getId();
        List<CartDetail> cartDetails = this.cartDetailService.getCartDetailByCartId(idCart);
        List<Review> reviews = this.reviewService.findAllByUserId(id);
        for (CartDetail c : cartDetails) {
            this.cartDetailService.deleteCartDetail(c.getId());
        }
        for (Review r : reviews) {
            this.reviewService.delete(r.getId());
        }
        this.cartService.deleteCart(idCart);
        if (this.userService.deleteUser(id)) {
            return "redirect:/admin/user";
        } else {
            return "redirect:/admin/user";
        }
    }

    @GetMapping("/admin/change-password-user/{id}")
    public String showChangePasswordPage(@PathVariable("id") Long id, Model model) {
        this.getUser(model);
        model.addAttribute("id", id);
        return "admin/user/changePassword";
    }

    @PostMapping("/admin/change-password-user")
    public String changePassword(@RequestParam("id") Long id,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("newPassword2") String newPassword2,
                                 Model model) {
        User user = this.getUser(model);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("message", "Current password is incorrect");
            model.addAttribute("id", id);
            ;
            return "admin/user/changePassword";
        }
        if (newPassword.equals(newPassword2)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            this.userService.updateUser(user);
            model.addAttribute("message", "Change password completed");
            model.addAttribute("success", true);
            return "redirect:/admin/user";
        } else {
            model.addAttribute("message", "New password doesn't match");
            model.addAttribute("id", id);
            return "admin/user/changePassword";
        }
    }

    public User getUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
            model.addAttribute("user", user);
            return user;
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            user = this.userService.findByEmail(oidcUser.getEmail());
            model.addAttribute("user", user);
            return user;
        }else if (authentication.getPrincipal() instanceof OAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            user = this.userService.findByEmail(oAuth2User.getAttribute("email"));
            return user;
        } else {
            throw new IllegalStateException("Unexpected user type: " + principal.getClass());
        }
    }
}
