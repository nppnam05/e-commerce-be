package com.e_commerce.e_commerce_api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.e_commerce.e_commerce_api.entity.Product;
import com.e_commerce.e_commerce_api.projection.ProductDetailProjection;
import com.e_commerce.e_commerce_api.projection.ProductFilterProjection;
import com.e_commerce.e_commerce_api.projection.ProductProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    @Query(value = """
            SELECT p."Id" AS "id",
            p."Name" AS "name",
            p."Description" AS "description",
            p."Price" AS "price",
            STRING_AGG(pi."Url", ',') AS "imageUrls"
            FROM "inventories"."products" p
            LEFT JOIN "inventories"."product_images" pi ON p."Id" = pi."ProductId"
            WHERE
            p."Status" = 'ACT'
            AND LOWER(p."Name") LIKE LOWER(CONCAT('%', :keyword, '%'))
            GROUP BY p."Id"
            ORDER BY p."CreatedOn" DESC
            LIMIT :pageSize
            OFFSET :offset
            """, nativeQuery = true)
    List<ProductProjection> findAllProducts(@Param("keyword") String keyword,
            @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query("SELECT COUNT(p.id) FROM Product p WHERE p.status = 'ACT' AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    long countProducts(@Param("keyword") String keyword);

    @Query(value = """
            SELECT p."Id" AS "id",
            p."Name" AS "name",
            p."Description" AS "description",
            p."Price" AS "price",
            p."CategoryId" AS "categoryId",
            STRING_AGG(pi."Url", ',') AS "imageUrls"
            FROM "inventories"."products" p
            LEFT JOIN "inventories"."product_images" pi ON p."Id" = pi."ProductId"
            WHERE
            p."Status" = 'ACT'
            AND p."Id" = :id
            GROUP BY p."Id"
            ORDER BY p."CreatedOn" DESC
            """, nativeQuery = true)
    Optional<ProductDetailProjection> findProductById(@Param("id") Long id);

    @Query(value = """
            SELECT p."Id" AS "id",
            p."Name" AS "name"
            FROM "inventories"."products" p
            WHERE
            p."Status" = 'ACT'
            """, nativeQuery = true)
    List<ProductFilterProjection> findProductFilters();

    List<Product> findByCategoryId(Long categoryId);

    boolean existsByName(String name);
}
