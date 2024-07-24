package com.example.ecommerce.services;

import com.example.ecommerce.entities.Role;
import com.example.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getById(Long id);

    Boolean createUser(User user);

    Boolean updateUser(User user);

    Boolean deleteUser(Long id);

    User getByEmailAndPassword(String email, String pass);

    void registerUser(User user);

    Boolean activateUser(String code);

    void sendActivationEmail(User user);

    User findByActivationCode(String activationCode);

    public void saveUser(User user);

    User findByEmail(String email);

    Role findByRoleName(String roleName);


    public void logout();

    Page<User> findAll(int pageNo, int size);
}
