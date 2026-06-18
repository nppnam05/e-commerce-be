package com.e_commerce.e_commerce_api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.e_commerce.e_commerce_api.entity.Cart;
import com.e_commerce.e_commerce_api.projection.CartWithProductProjection;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(value = """
            select
            	c."Id" as "id",
            	c."UserId" as "userId",
            	c."ProductId" as "productId",
            	c."Quantity" as "quantity",
            	p."Price" as "singlePrice",
            	p."Name" as "productName",
            	size."Name" as "size",
            	color."Name" as "colorCode",
            	image.imageurl as "imageUrls"
            from
            	"sales".carts c
            left join
            	"inventories".products p
            on
            	c."ProductId" = p."Id"
            left join
            	"inventories".colors color
            on
            	p."ColorId" = color."Id"
            left join
            	"inventories".sizes size
            on
            	p."SizeId" = size."Id"
            left join (
            	select t."productId",
            	STRING_AGG(t.url, ', ') as imageUrl
            	from "inventories".product_images t
            	group by
            		t."productId"
            ) image
            on p."Id" = image."productId"
            where
                c."UserId" = :userId
                """, nativeQuery = true)
    List<CartWithProductProjection> findByUserId(@Param("userId") Long userId);


    // @Query(value = """
    // SELECT
    // (SELECT COUNT(*) FROM "identity"."users" WHERE "Status" = 'ACT') AS
    // "totalUsers",
    // (SELECT COUNT(*) FROM "sales"."orders" WHERE "Status" = 'PND') AS
    // "totalPending",
    // (SELECT COUNT(*) FROM "sales"."orders" WHERE "Status" = 'COM') AS
    // "totalSales",
    // (SELECT COUNT(*) FROM "sales"."orders") AS "totalOrders"
    //
    // """, nativeQuery = true)
    // @Query(value = """
    // SELECT * FROM "sales"."carts" c LEFT JOIN "inventories"."products" p
    // """, nativeQuery = true)
    @Query(value = """
            select
            	c."Id" as "id",
            	c."UserId" as "userId",
            	c."ProductId" as "productId",
            	c."Quantity" as "quantity",
            	p."Price" as "singlePrice",
            	p."Name" as "productName",
            	size."Name" as "size",
            	color."Name" as "colorCode",
            	image.imageurl as "imageUrls"
            from
            	"sales".carts c
            left join
            	"inventories".products p
            on
            	c."ProductId" = p."Id"
            left join
            	"inventories".colors color
            on
            	p."ColorId" = color."Id"
            left join
            	"inventories".sizes size
            on
            	p."SizeId" = size."Id"
            left join (
            	select t."productId",
            	STRING_AGG(t.url, ', ') as imageUrl
            	from "inventories".product_images t
            	group by
            		t."productId"
            ) image
            on p."Id" = image."productId"
                """, nativeQuery = true)
    List<CartWithProductProjection> findAllWithProducts();
}
