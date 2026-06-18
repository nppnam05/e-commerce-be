package com.e_commerce.e_commerce_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.e_commerce.e_commerce_api.entity.ProductChildren;
import com.e_commerce.e_commerce_api.projection.ProductChildrenProjection;

public interface ProductChildrenRepository extends JpaRepository<ProductChildren, Long> {
    @Query(value = """
            SELECT pc."Id" AS "id",
            s."Quantity" AS "quantity",
            c."ColorCode" AS "colorCode",
            sz."Name" AS "size",
            p."Name" AS "name",
            p."Price" AS "price",
            ca."Name" AS "category",
            STRING_AGG(pi."Url", ',') AS "imageUrls"
            FROM "inventories"."product_children" pc
            LEFT JOIN "inventories"."stocks" s ON s."ProductChildrenId" = pc."Id"
            LEFT JOIN "inventories"."products" p ON pc."ProductId" = p."Id"
            LEFT JOIN "inventories"."colors" c ON pc."ColorId" = c."Id"
            LEFT JOIN "inventories"."sizes" sz ON pc."SizeId" = sz."Id"
            LEFT JOIN "inventories"."product_images" pi ON pi."ProductId" = p."Id"
            LEFT JOIN "inventories"."categories" ca ON p."CategoryId" = ca."Id"
            WHERE
            pc."Status" = 'ACT'
            AND LOWER(p."Name") LIKE LOWER(CONCAT('%', :keyword, '%'))
            AND (:productId IS NULL OR p."Id" = :productId)
            GROUP BY pc."Id", s."Quantity", c."ColorCode", sz."Name", p."Name", p."Price", ca."Name", pc."CreatedOn"
            ORDER BY pc."CreatedOn" DESC
            LIMIT :pageSize
            OFFSET :offset
            """,
            nativeQuery = true)
    List<ProductChildrenProjection> findAllProductChildren(@Param("keyword") String keyword,
            @Param("pageSize") int pageSize, @Param("offset") int offset,
            @Param("productId") Integer productId);

    @Query("SELECT COUNT(pc.id) FROM ProductChildren pc WHERE pc.status = 'ACT' "
            + "AND LOWER(pc.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "AND (:productId IS NULL OR pc.product.id = :productId)")
    long countProductChildren(@Param("keyword") String keyword,
            @Param("productId") Integer productId);
}
