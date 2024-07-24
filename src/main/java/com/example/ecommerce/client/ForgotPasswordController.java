package com.example.ecommerce.client;

import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.services.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class ForgotPasswordController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "client/login/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String sendEmailForgotPassword(@RequestParam("email") String email, Model model) {
        User user = this.userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("message", "Email is incorrect");
            return "client/login/forgotPassword";
        }
        String token = UUID.randomUUID().toString();
        user.setActivationCode(token);
        userService.updateUser(user);
        this.emailService.sendResetPassword(user.getEmail(), token);
        model.addAttribute("message", "A password reset link has been sent to your email.");
        return "client/login/forgotPassword";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        User user = this.userService.findByActivationCode(token);
        if (user == null) {
            model.addAttribute("message", "Invalid token");
            return "client/login/resetPassword";
        }
        model.addAttribute("token", token);
        return "client/login/resetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                @RequestParam("rePassword") String rePassword,
                                Model model) {
        User user = this.userService.findByActivationCode(token);
        System.out.println(user.getEmail());
        if (user == null) {
            model.addAttribute("message", "Invalid token");
            return "client/login/resetPassword";
        }
        if (password.equals(rePassword)) {
            user.setPassword(passwordEncoder.encode(password));
            user.setActivationCode(null);
            this.userService.updateUser(user);
            model.addAttribute("message", "Password has been reset successfully.");
            return "redirect:/login";
        } else {
            model.addAttribute("message", "The password doesn't match");
            return "client/login/resetPassword";
        }
    }
}
