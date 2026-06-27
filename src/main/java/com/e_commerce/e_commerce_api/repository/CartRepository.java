package com.e_commerce.e_commerce_api.repository;

import com.e_commerce.e_commerce_api.entity.Cart;
import com.e_commerce.e_commerce_api.projection.CartWithProductProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("""
            SELECT c FROM Cart c
            JOIN FETCH c.user u
            JOIN FETCH c.productChildren pc
            JOIN FETCH pc.product p
            WHERE c.userId = :id
            """)
    List<Cart> findByUserIdWithDetail(@Param("id") Long id);

    @Query(value = """
            select
            	c."Id" as "id",
            	c."UserId" as "userId",
            	c."ProductChildrenId" as "productChildrenId",
            	c."Quantity" as "quantity",
            	c."SinglePrice" as "singlePrice",
            	p."Name" as "productName",
            	size."Name" as "size",
            	color."Name" as "colorCode",
            	image.imageUrl as "imageUrls"
            from
            	"sales".carts c
            left join
            	"inventories".product_children pc
            on
            	c."ProductChildrenId" = pc."Id"
            left join
                "inventories".products p
            on
                pc."ProductId" = p."Id"
            left join
            	"inventories".colors color
            on
            	pc."ColorId" = color."Id"
            left join
            	"inventories".sizes size
            on
            	pc."SizeId" = size."Id"
            left join (
            	select t."ProductId",
            	STRING_AGG(t."Url", ', ') as imageUrl
            	from "inventories".product_images t
            	group by
            		t."ProductId"
            ) image
            on p."Id" = image."ProductId"
            where
                c."UserId" = :userId
                """, nativeQuery = true)
    List<CartWithProductProjection> findByUserId(@Param("userId") Long userId);

    @Query(value = """
            select
            	c."Id" as "id",
            	c."UserId" as "userId",
            	c."Quantity" as "quantity",
                c."ProductChildrenId" as "productChildrenId",
            	c."SinglePrice" as "singlePrice",
            	p."Name" as "productName",
            	size."Name" as "size",
            	color."Name" as "colorCode",
            	image.imageUrl as "imageUrls"
            from
            	"sales".carts c
            left join
            	"inventories".product_children pc
            on
            	c."ProductChildrenId" = pc."Id"
            left join
                "inventories".products p
            on
                pc."ProductId" = p."Id"
            left join
            	"inventories".colors color
            on
            	p."ColorId" = color."Id"
            left join
            	"inventories".sizes size
            on
            	p."SizeId" = size."Id"
            left join (
            	select t."ProductId",
            	STRING_AGG(t.url, ', ') as imageUrl
            	from "inventories".product_images t
            	group by
            		t."ProductId"
            ) image
            on p."Id" = image."ProductId"
                """, nativeQuery = true)
    List<CartWithProductProjection> findAllWithProducts();

    void deleteAllByUserId(Long userId);
}
