package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Color;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByName(String name);

    Optional<Color> findByColorCode(String colorCode);

    boolean existsByName(String name);

    @Query("SELECT DISTINCT pc.color FROM ProductChildren pc "
            + "JOIN Stock s ON s.productChildren.id = pc.id " + "WHERE pc.productId = :productId "
            + "AND pc.status = 'ACT' " + "AND pc.color.status = 'ACT' " + "AND s.status = 'ACT' "
            + "AND s.quantity > 0")
    List<Color> findColorsWithStockByProductId(@Param("productId") Long productId);

}
