package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Stock;
import com.e_commerce.e_commerce_api.projection.ProductStockProjection;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.productChildren.id IN :productChildrenIds")
    List<Stock> findStocksByProductChildrenIdsForUpdate(
            @Param("productChildrenIds") List<Long> productChildrenIds);

    @Query(value = """
            SELECT s."Id" AS "id",
            s."Quantity" AS "quantity",
            c."ColorCode" AS "colorCode",
            sz."Name" AS "size",
            p."Name" AS "name",
            p."Price" AS "price",
            ca."Name" AS "category",
            STRING_AGG(pi."Url", ',') AS "imageUrls"
            FROM "inventories"."stocks" s
            LEFT JOIN "inventories"."product_children" pc ON s."ProductChildrenId" = pc."Id"
            LEFT JOIN "inventories"."products" p ON pc."ProductId" = p."Id"
            LEFT JOIN "inventories"."colors" c ON pc."ColorId" = c."Id"
            LEFT JOIN "inventories"."sizes" sz ON pc."SizeId" = sz."Id"
            LEFT JOIN "inventories"."product_images" pi ON pi."ProductId" = p."Id"
            LEFT JOIN "inventories"."categories" ca ON p."CategoryId" = ca."Id"
            WHERE
            s."Status" = 'ACT'
            AND LOWER(p."Name") LIKE LOWER(CONCAT('%', :keyword, '%'))
            GROUP BY s."Id", s."Quantity", c."ColorCode", sz."Name", p."Name", p."Price", ca."Name", s."CreatedOn"
            ORDER BY s."CreatedOn" DESC
            LIMIT :pageSize
            OFFSET :offset
            """,
            nativeQuery = true)
    List<ProductStockProjection> findAllStocks(@Param("keyword") String keyword,
            @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query("SELECT COUNT(s.id) FROM Stock s WHERE s.status = 'ACT' AND LOWER(s.productChildren.product.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    long countStocks(@Param("keyword") String keyword);
}
