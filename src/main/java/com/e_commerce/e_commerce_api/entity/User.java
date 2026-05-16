package com.e_commerce.e_commerce_api.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", schema = "identity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(name = "RoleId", insertable = false, updatable = false)
    private Long roleId;

    @Column(length = 255, name = "Email")
    private String email;

    @Column(length = 200, name = "DisplayName")
    private String displayName;

    @Column(length = 500, name = "Avatar")
    private String avatar;

    @Column(length = 20, name = "Phone")
    private String phone;

    @Column(length = 200, name = "Location")
    private String location;

    @Column(length = 500, name = "PasswordHash")
    private String passwordHash;

    @Column(name = "FailedLoginAttempts")
    private int failedLoginAttempts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserSession> userSessions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleId")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
