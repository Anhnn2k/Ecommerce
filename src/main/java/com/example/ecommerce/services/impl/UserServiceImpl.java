package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Cart;
import com.example.ecommerce.entities.Role;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.repositories.RoleRepository;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.RoleService;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CartService cartService;

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return this.userRepository.findById(id).get();
    }

    @Override
    public Boolean createUser(User user) {
        try {
            this.userRepository.save(user);
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setStatus(true);
            this.cartService.createCart(cart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean updateUser(User user) {
        try {
            this.userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteUser(Long id) {
        try {
            this.userRepository.delete(this.userRepository.findById(id).get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getByEmailAndPassword(String email, String pass) {
        return this.userRepository.getByEmailAndPassword(email, pass);
    }

    @Override
    public void registerUser(User user) {
        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);
        user.setStatus(false);
        user.setRole(roleService.getById(Long.valueOf(2)));
        user.setImageUrl("/uploads/Avatar-mac-dinh.png");
        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);

        // Gửi email kích hoạt
        emailService.sendActivationEmail(user.getEmail(), activationCode);
    }

    @Override
    public Boolean activateUser(String activationCode) {
        User user = userRepository.findByActivationCode(activationCode);
        if (user != null) {
            user.setStatus(true);
            user.setActivationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void sendActivationEmail(User user) {
    }

    @Override
    public User findByActivationCode(String activationCode) {
        return this.userRepository.findByActivationCode(activationCode);
    }

    @Override
    public void saveUser(User user) {
        Role role = this.roleService.getById(Long.valueOf(2));
        user.setRole(role);
        this.userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }


    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public Page<User> findAll(int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        return this.userRepository.findAll(pageable);
    }
}
