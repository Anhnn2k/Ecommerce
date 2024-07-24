package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Color;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductDetails;
import com.example.ecommerce.entities.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDetaisRepository extends JpaRepository<ProductDetails, Long> {
    Page<ProductDetails> findAll(Pageable pageable);

    Page<ProductDetails> findAllByProductId(Pageable pageable, Long id);

    @Query("select p from ProductDetails p where p.product.id=?1")
    List<ProductDetails> getByProductId(Long id);

    @Query("select distinct c from ProductDetails p join Color c on p.color.id=c.id where p.product.id=?1")
    List<Color> getCoLorByProductId(Long id);

    @Query("select distinct s from ProductDetails p join Size s on p.size.id=s.id where p.product.id=?1")
    List<Size> getSizeByProductId(Long id);

    @Query("select pd from ProductDetails pd where pd.product.id=?1 and pd.size.id=?2 and pd.color.id=?3")
    ProductDetails getByProductAndSizeAndColor(Long idProduct, Long idSize, Long idColor);

    @Query("select p.product from ProductDetails p order by p.quantity asc ")
    List<Product> getHotProductDetais();
}
