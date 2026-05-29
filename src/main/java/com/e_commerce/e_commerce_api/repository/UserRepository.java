package com.e_commerce.e_commerce_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.e_commerce.e_commerce_api.entity.User;
import com.e_commerce.e_commerce_api.projection.TotalProjection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);

    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userSessions WHERE u.id = :id")
    Optional<User> findByIdWithSessions(@Param("id") Long id);

    @Query(value = """
            SELECT
                (SELECT COUNT(*) FROM "identity"."users" WHERE "Status" = 'ACT') AS "totalUsers",
                (SELECT COUNT(*) FROM "sales"."orders" WHERE "Status" = 'PEN') AS "totalPending",
                (SELECT COUNT(*) FROM "sales"."orders" WHERE "Status" = 'COM') AS "totalSales",
                (SELECT COUNT(*) FROM "sales"."orders") AS "totalOrders"

            """, nativeQuery = true)
    TotalProjection getTotal();
}
