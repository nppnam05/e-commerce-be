package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Size;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT DISTINCT pc.size FROM ProductChildren pc "
            + "JOIN Stock s ON s.productChildren.id = pc.id " + "WHERE pc.productId = :productId "
            + "AND pc.status = 'ACT' " + "AND pc.size.status = 'ACT' " + "AND s.status = 'ACT' "
            + "AND s.quantity > 0")
    List<Size> findSizesWithStockByProductId(@Param("productId") Long productId);

}
