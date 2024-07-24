package com.example.ecommerce.dto;

import com.example.ecommerce.entities.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            if (roles.contains("ADMIN")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/home");
            }
        } else if (authentication.getPrincipal() instanceof OidcUser) {
            if (roles.contains("ADMIN")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/home");
            }
        }else if (authentication.getPrincipal() instanceof OAuth2User) {
            if (roles.contains("ADMIN")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/home");
            }
        }
        else {
            throw new IllegalStateException("Unexpected user type: " + authentication.getPrincipal().getClass());
        }
    }
}
