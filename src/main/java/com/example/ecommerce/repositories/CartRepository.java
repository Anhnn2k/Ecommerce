package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Cart;
import com.example.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c where c.user.id=?1")
    Cart getByUserId(Long id);

    @Query("select c.user from Cart c where c.id=?1")
    User getUserByCartId(Long id);
}
