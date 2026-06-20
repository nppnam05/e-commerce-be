package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Size;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findByName(String name);

    boolean existsByName(String name);
}
