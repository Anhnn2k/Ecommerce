package com.example.ecommerce.services;

import com.example.ecommerce.entities.Color;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductDetails;
import com.example.ecommerce.entities.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductDetailsService {
    List<ProductDetails> getAll();

    ProductDetails getById(Long id);

    Boolean create (ProductDetails productDetails);

    Boolean update (ProductDetails productDetails);

    Boolean delete (Long id);
    Page<ProductDetails> getAllProducts(int size, int pageNo);

    List<ProductDetails> getByProductId(Long id);

    Page<ProductDetails> findAllByProductId(int pageNo, int size, Long id);

    List<Color> getCoLorByProductId(Long id);

    List<Size> getSizeByProductId(Long id);

    ProductDetails getByProductAndSizeAndColor(Long idProduct, Long idSize, Long idColor);

    List<Product> getHotProductDetais();

}
