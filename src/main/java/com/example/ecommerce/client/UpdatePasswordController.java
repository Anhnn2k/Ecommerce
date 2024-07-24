package com.example.ecommerce.client;

import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UpdatePasswordController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/home/update-password")
    public String updatePassword(Model model) {
        User user = getUserByType();
        model.addAttribute("id1", user.getId());
        return "client/profile/updatePassword";
    }

    @PostMapping("/home/update-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("newPassword2") String newPassword2,
                                 Model model) {
        User user = getUserByType();
        if (user == null) {
            model.addAttribute("message", "User not found");
            model.addAttribute("id1", user.getId());
            return "client/profile/updatePassword";
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("message", "Current password is incorrect");
            return "client/profile/updatePassword";
        }
        if (newPassword.equals(newPassword2)) {
            String newPasswordEncoded = passwordEncoder.encode(newPassword);
            user.setPassword(newPasswordEncoded);
            userService.updateUser(user);
            model.addAttribute("message", "Change password completed");
            String url = "redirect:/home/client/profile/" + user.getId();
            return url;
        } else {
            model.addAttribute("message", "New password doesn't match");
            model.addAttribute("id1", user.getId());
            return "client/profile/updatePassword";
        }
    }

    public User getUserByType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
            return user;
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            user = this.userService.findByEmail(oidcUser.getEmail());
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
