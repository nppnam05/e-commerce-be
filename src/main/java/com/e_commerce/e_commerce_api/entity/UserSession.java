package com.e_commerce.e_commerce_api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_session", schema = "identity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserSession extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(name = "UserId", insertable = false, updatable = false)
    private Long userId;

    @Column(length = 500, name = "SessionToken")
    private String sessionToken;

    @Column(length = 500, name = "RefreshToken")
    private String refreshToken;

    @Column(length = 500, name = "DeviceInfo")
    private String deviceInfo;

    @Column(length = 45, name = "IpAddress")
    private String ipAddress;

    @Column(length = 1000, name = "UserAgent")
    private String userAgent;

    @Column(name = "ExpiresAt")
    private LocalDateTime expiresAt;

    @Column(name = "LastAccessedOn")
    private LocalDateTime lastAccessedOn;

    @Column(length = 100, name = "DeviceId")
    private String deviceId;

    @Column(name = "RevokedOn")
    private LocalDateTime revokedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;
}
