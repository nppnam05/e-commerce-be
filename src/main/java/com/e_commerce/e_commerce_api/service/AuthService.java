package com.e_commerce.e_commerce_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.constant.NameTypeToken;
import com.e_commerce.e_commerce_api.constant.TypeJwt;
import com.e_commerce.e_commerce_api.dto.request.auth.LoginRequest;
import com.e_commerce.e_commerce_api.dto.request.user.CreateUserRequest;
import com.e_commerce.e_commerce_api.dto.response.UserResponse;
import com.e_commerce.e_commerce_api.entity.UserSession;
import com.e_commerce.e_commerce_api.exception.BadRequestException;
import com.e_commerce.e_commerce_api.exception.LoginFailedException;
import com.e_commerce.e_commerce_api.exception.NotFoundException;
import com.e_commerce.e_commerce_api.exception.UnauthorizedException;
import com.e_commerce.e_commerce_api.mapper.UserMapper;
import com.e_commerce.e_commerce_api.repository.RoleRepository;
import com.e_commerce.e_commerce_api.repository.UserRepository;
import com.e_commerce.e_commerce_api.repository.UserSessionRepository;
import com.e_commerce.e_commerce_api.utils.CookieUtils;
import com.e_commerce.e_commerce_api.utils.DateTimeUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Transactional(dontRollbackOn = LoginFailedException.class)
    public UserResponse login(LoginRequest request, HttpServletResponse response) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 1. Kiểm tra khóa tài khoản TRƯỚC khi làm bất cứ việc gì
        if (user.getFailedLoginAttempts() >= 5) {
            throw new LoginFailedException(
                    "Account is locked due to too many failed attempts. Please contact support.");
        }

        try {
            // 2. Thử xác thực
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // 3. Nếu thành công -> Reset số lần sai
            user.setFailedLoginAttempts(0);
            userRepository.save(user);

            // 4. Tạo token và lưu Cookie
            String accessToken = jwtService.generateToken(user, TypeJwt.ACCESS);
            String refreshToken = jwtService.generateToken(user, TypeJwt.REFRESH);

            CookieUtils.addCookie(response, NameTypeToken.accessToken.name(), accessToken,
                    (int) (accessExpiration / 1000), cookieSecure);
            CookieUtils.addCookie(response, NameTypeToken.refreshToken.name(), refreshToken,
                    (int) (refreshExpiration / 1000), cookieSecure);

            // 5. Quản lý Session
            UserSession session;
            Optional<UserSession> existingSession = userSessionRepository.findByUser(user);

            if (existingSession.isEmpty()) {
                session = UserSession.builder()
                        .user(user)
                        .refreshToken(refreshToken)
                        .sessionToken(accessToken)
                        .expiresAt(DateTimeUtils.toLocalDateTime(System.currentTimeMillis() + refreshExpiration))
                        .build();
            } else {
                session = existingSession.get();
                session.setRefreshToken(refreshToken);
                session.setSessionToken(accessToken);
                session.setExpiresAt(DateTimeUtils.toLocalDateTime(System.currentTimeMillis() + refreshExpiration));
                session.setModifiedBy(user.getEmail());
            }
            userSessionRepository.save(session);

            return userMapper.toResponse(user);

        } catch (BadCredentialsException e) {
            // 6. Sai mật khẩu -> Tăng biến đếm và LƯU NGAY
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);

            int remaining = 5 - user.getFailedLoginAttempts();
            String msg = (remaining > 0)
                    ? "Incorrect email or password. You have " + remaining + " attempts left."
                    : "Account has been locked.";
            throw new LoginFailedException(msg);
        }
    }

    @Transactional
    public void refreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || !jwtService.isTokenValid(refreshToken, TypeJwt.REFRESH)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String email = jwtService.extractSubject(refreshToken, TypeJwt.REFRESH);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Kiểm tra token có khớp với token lưu trong DB không
        var session = userSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Token has been revoked or used"));

        if (session.getRevokedOn() != null) {
            throw new UnauthorizedException("Token has been revoked");
        }

        String newAccessToken = jwtService.generateToken(user, TypeJwt.ACCESS);
        String newRefreshToken = jwtService.generateToken(user, TypeJwt.REFRESH);
        CookieUtils.addCookie(response, NameTypeToken.accessToken.name(), newAccessToken,
                (int) (accessExpiration / 1000), cookieSecure);
        CookieUtils.addCookie(response, NameTypeToken.refreshToken.name(), newRefreshToken,
                (int) (refreshExpiration / 1000), cookieSecure);

        session.setSessionToken(newAccessToken);
        session.setRefreshToken(newRefreshToken);
        session.setLastAccessedOn(DateTimeUtils.toDateTimeNow());
        session.setExpiresAt(DateTimeUtils.toLocalDateTime(System.currentTimeMillis() + refreshExpiration));
        userSessionRepository.save(session);
    }

    @Transactional
    public void logout(String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            userSessionRepository.findByRefreshToken(refreshToken)
                    .ifPresent(userSessionRepository::delete);
        }
        CookieUtils.deleteCookie(response, NameTypeToken.accessToken.name(), cookieSecure);
        CookieUtils.deleteCookie(response, NameTypeToken.refreshToken.name(), cookieSecure);
    }

    public UserResponse signUp(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        var user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleRepository.findByName("USER").get());

        return userMapper.toResponse(userRepository.save(user));
    }
}
