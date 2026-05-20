package com.e_commerce.e_commerce_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.e_commerce.e_commerce_api.entity.User;
import com.e_commerce.e_commerce_api.entity.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    @Query("SELECT u FROM UserSession u WHERE u.deviceId = :deviceId AND u.status = :status")
    Optional<UserSession> findByDeviceId(String deviceId, String status);

    Optional<UserSession> findByRefreshToken(String refreshToken);

    Optional<UserSession> findBySessionToken(String sessionToken);

    Optional<UserSession> findByUser(User user);
}
