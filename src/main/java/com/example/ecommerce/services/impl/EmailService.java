package com.example.ecommerce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendActivationEmail(String to, String activationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Account Activation");
        message.setText("Please click the link to activate your account: "
                + "http://localhost:8080/activate?code=" + activationCode);

        javaMailSender.send(message);
    }

    public void sendResetPassword(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Please  click the link to reset password: " + "http://localhost:8080/reset-password?token=" + token);
        javaMailSender.send(message);
    }
}
