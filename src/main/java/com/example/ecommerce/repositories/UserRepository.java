package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email=?1 and u.password=?2")
    User getByEmailAndPassword(String email, String pass);

    User findByEmail(String email);

    User findByActivationCode(String activationCode);

    Page<User> findAll(Pageable pageable);
}
