package com.e_commerce.e_commerce_api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.e_commerce.e_commerce_api.entity.Order;
import com.e_commerce.e_commerce_api.projection.MonthlyRevenueProjection;
import com.e_commerce.e_commerce_api.projection.OrderDetailProjection;
import com.e_commerce.e_commerce_api.projection.OrderProjection;
import com.e_commerce.e_commerce_api.projection.ProductOrderProjection;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query(value = """
            SELECT
                o."Id" AS "id",
                o."Status" AS "status",
                o."Code" AS "code",
                o."CreatedOn" AS "createdOn",
                u."DisplayName" AS "customerName",
                o."Address" AS "address"
            FROM "sales"."orders" o
            LEFT JOIN "identity"."users" u ON o."UserId" = u."Id"
            WHERE (CAST(:dateTime AS date) IS NULL OR CAST(o."CreatedOn" AS date) = :dateTime)
                AND (CAST(:status AS varchar) IS NULL OR o."Status" = :status)
            ORDER BY o."CreatedOn" DESC
            LIMIT :pageSize
            OFFSET :offset
                                                """, nativeQuery = true)
    List<OrderProjection> findAllOrder(
            @Param("dateTime") LocalDate dateTime, @Param("status") String status,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset);

    @Query(value = """
            SELECT
                EXTRACT(MONTH FROM o."CreatedOn") AS "month",
                COALESCE(SUM(op."SinglePrice" * op."Quantity"), 0) AS "revenue"
            FROM "sales"."orders" o
            LEFT JOIN "sales"."order_products" op ON o."Id" = op."OrderId"
            WHERE o."Status" = 'COM'
              AND EXTRACT(YEAR FROM o."CreatedOn") = EXTRACT(YEAR FROM CURRENT_DATE)
            GROUP BY EXTRACT(MONTH FROM o."CreatedOn")
            ORDER BY "month" ASC
            """, nativeQuery = true)
    List<MonthlyRevenueProjection> getMonthlyRevenueForThisYear();

    @Query(value = """
            SELECT
                o."Id" AS "id", o."Status" AS "status", o."Code" AS "code",
                o."CreatedOn" AS "createdOn", o."Address" AS "address",
                u."DisplayName" AS "customerName",
                SUM(op."SinglePrice" * op."Quantity") AS "totalAmount"
            FROM "sales"."orders" o
            LEFT JOIN "identity"."users" u ON o."UserId" = u."Id"
            LEFT JOIN "sales"."order_products" op ON o."Id" = op."OrderId"
            WHERE o."Id" = :id
            GROUP BY o."Id", u."Id"
            """, nativeQuery = true)
    Optional<OrderDetailProjection> findOrderDetailById(@Param("id") Long id);

    @Query(value = """
            SELECT
                p."Id" AS "id", p."Name" AS "name",
                op."SinglePrice" AS "price", op."Quantity" AS "quantity",
                cat."Name" AS "category", s."Name" AS "size", c."ColorCode" AS "colorCode",
                STRING_AGG(pi."url", ',') AS "imageUrls"
            FROM "sales"."order_products" op
            JOIN "inventories"."products" p ON op."ProductId" = p."Id"
            LEFT JOIN "inventories"."product_images" pi ON p."Id" = pi."productId"
            LEFT JOIN "inventories"."colors" c ON p."ColorId" = c."Id"
            LEFT JOIN "inventories"."sizes" s ON p."SizeId" = s."Id"
            LEFT JOIN "inventories"."categories" cat ON p."CategoryId" = cat."Id"
            WHERE op."OrderId" = :id
            GROUP BY p."Id", op."Id", cat."Id", s."Id", c."Id"
            """, nativeQuery = true)
    List<ProductOrderProjection> findProductsByOrderId(@Param("id") Long id);

    @Query(value = """
                                        SELECT COUNT(*)
                                        FROM "sales"."orders" o
                                       WHERE (CAST(:dateTime AS date) IS NULL OR CAST(o."CreatedOn" AS date) = :dateTime)
            AND (CAST(:status AS varchar) IS NULL OR o."Status" = :status)
                                        """, nativeQuery = true)
    long countOrders(@Param("dateTime") LocalDate dateTime, @Param("status") String status);
}
