package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Address;
import com.e_commerce.e_commerce_api.projection.AddressUserProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(value = """
            SELECT a."Id" as "id",
            a."UserId" as "userId",
            a."City" as "city",
            a."Street" as "street",
            a."District" as "district",
            a."Ward" as "ward",
            a."IsDefault" as "isDefault"
            FROM "identity"."addresses" a
            WHERE a."UserId" = :userId
            ORDER BY a."IsDefault" DESC
            LIMIT :pageSize OFFSET :offset
            """, nativeQuery = true)
    List<AddressUserProjection> findByUserId(Long userId, int pageSize, int offset);

    @Query(value = """
            SELECT count(a."Id")
            FROM "identity"."addresses" a
            WHERE a."UserId" = :userId
            """, nativeQuery = true)
    long countByUserId(Long userId);
}
