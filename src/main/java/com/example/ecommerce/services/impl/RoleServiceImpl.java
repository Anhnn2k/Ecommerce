package com.example.ecommerce.services.impl;

import com.example.ecommerce.entities.Role;
import com.example.ecommerce.repositories.RoleRepository;
import com.example.ecommerce.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return this.roleRepository.findAll();
    }

    @Override
    public Role getById(Long id) {
        return this.roleRepository.findById(id).get();
    }

    @Override
    public Boolean create(Role role) {
        return null;
    }

    @Override
    public Boolean update(Role role) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
}
