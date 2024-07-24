package com.example.ecommerce.services;

import com.example.ecommerce.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Role getById(Long id);

    Boolean create(Role role);

    Boolean update(Role role);

    Boolean delete(Long id);
}
