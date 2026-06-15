package com.e_commerce.e_commerce_api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.e_commerce.e_commerce_api.entity.Product;
import com.e_commerce.e_commerce_api.projection.ProductDetailProjection;
import com.e_commerce.e_commerce_api.projection.ProductProjection;
import com.e_commerce.e_commerce_api.projection.ProductStockProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    @Query(value = """
            SELECT p."Id" AS "id",
            p."Name" AS "name",
            p."Description" AS "description",
            p."Price" AS "price",
            STRING_AGG(pi."url", ',') AS "imageUrls"
            FROM "inventories"."products" p
            LEFT JOIN "inventories"."product_images" pi ON p."Id" = pi."productId"
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

    @Query(value = """
            SELECT p."Id" AS "id",
            p."Name" AS "name",
            p."Description" AS "description",
            p."Price" AS "price",
            STRING_AGG(pi."url", ',') AS "imageUrls",
            p."CategoryId" AS "categoryId",
            p."ColorId" AS "colorId",
            p."SizeId" AS "sizeId",
            c."ColorCode" AS "colorCode"
            FROM "inventories"."products" p
            LEFT JOIN "inventories"."product_images" pi ON p."Id" = pi."productId"
            LEFT JOIN "inventories"."colors" c ON p."ColorId" = c."Id"
            WHERE
            p."Status" = 'ACT'
            AND p."Id" = :id
            GROUP BY p."Id", c."ColorCode"
            """, nativeQuery = true)
    Optional<ProductDetailProjection> findProductDetailById(@Param("id") Long id);

    @Query(value = """
            SELECT p."Id" AS "id",
            p."Name" AS "name",
            p."Price" AS "price",
            STRING_AGG(DISTINCT pi."url", ',') AS "imageUrls",
            cat."Name" AS "category",
            s."Name" AS "size",
            c."ColorCode" AS "color",
            st."Quantity" AS "quantity"
            FROM "inventories"."products" p
            LEFT JOIN "inventories"."product_images" pi ON p."Id" = pi."productId"
            LEFT JOIN "inventories"."colors" c ON p."ColorId" = c."Id"
            LEFT JOIN "inventories"."sizes" s ON p."SizeId" = s."Id"
            LEFT JOIN "inventories"."categories" cat ON p."CategoryId" = cat."Id"
            LEFT JOIN "inventories"."stocks" st ON p."Id" = st."ProductId"
            WHERE
            p."Status" = 'ACT'
            AND LOWER(p."Name") LIKE LOWER(CONCAT('%', :keyword, '%'))
            GROUP BY p."Id", cat."Name", st."Quantity", s."Name", c."ColorCode"
            ORDER BY p."CreatedOn" DESC
            LIMIT :pageSize
            OFFSET :offset
            """, nativeQuery = true)
    List<ProductStockProjection> findProductStocks(@Param("keyword") String keyword,
            @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query("SELECT COUNT(p.id) FROM Product p WHERE p.status = 'ACT' AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    long countProducts(@Param("keyword") String keyword);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByColorId(Long colorId);

    List<Product> findBySizeId(Long sizeId);

    boolean existsByName(String name);
}
