package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Product;
import com.e_commerce.e_commerce_api.entity.ProductImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);

    void deleteAllByProduct(Product product);
}
