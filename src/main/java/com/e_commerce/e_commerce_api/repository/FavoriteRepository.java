package com.e_commerce.e_commerce_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.e_commerce.e_commerce_api.entity.Favorite;
import com.e_commerce.e_commerce_api.projection.FavoriteProductProjection;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query(value = """
            SELECT f."Id" as "id",
                    f."UserId" as "userId",
                    p."Id" as "productId",
                    p."Price" as "price",
                    p."Name" as "name",
                    STRING_AGG(pi."url", ',') AS "imageUrls"
            FROM "inventories"."favourites" f
            INNER JOIN "inventories"."products" p ON f."ProductId" = p."Id"
            LEFT JOIN "inventories"."product_images" pi ON p."Id" = pi."productId"
            WHERE f."UserId" = :userId
            GROUP BY f."Id", p."Id", p."Price", p."Name"
            ORDER BY f."CreatedOn" DESC
            LIMIT :pageSize
            OFFSET :offset
            """, nativeQuery = true)
    List<FavoriteProductProjection> findByUserId(@Param("userId") Long userId,
            @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.userId = :userId")
    long countByUserId(Long userId);

    Optional<List<Favorite>> findByProductId(Long productId);
}
