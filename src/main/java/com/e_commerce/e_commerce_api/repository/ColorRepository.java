package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Color;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByName(String name);

    Optional<Color> findByColorCode(String colorCode);

    boolean existsByName(String name);
}
