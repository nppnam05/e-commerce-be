package com.e_commerce.e_commerce_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.constant.TypeJwt;
import com.e_commerce.e_commerce_api.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${fss.jwt.access-secret}")
    private String accessSecret;

    @Value("${fss.jwt.access-expiration}")
    private long accessExpiration;

    @Value("${fss.jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${fss.jwt.refresh-expiration}")
    private long refreshExpiration;

    // Chuyển chuỗi secret thành SecretKey để dùng cho JJWT 0.12+
    private SecretKey getSigningKey(TypeJwt type) {
        if (type == TypeJwt.ACCESS) {
            return Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        } else
            return Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractSubject(String token, TypeJwt type) {
        return extractClaim(token, Claims::getSubject, type);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, TypeJwt type) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey(type))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, TypeJwt type) {
        if (!(userDetails instanceof User user)) {
            throw new RuntimeException("Invalid user type");
        }

        long expiration = (type == TypeJwt.ACCESS)
                ? accessExpiration
                : refreshExpiration;

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", String.valueOf(user.getId()))
                .claim("typeToken", type.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(type))
                .compact();
    }

    public boolean isTokenValid(String token, TypeJwt type) {
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey(type))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenType = claims.get("typeToken", String.class);
            boolean isCorrectType = type.name().equals(tokenType);

            return isCorrectType && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}