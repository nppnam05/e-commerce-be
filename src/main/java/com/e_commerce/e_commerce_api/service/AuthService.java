package com.e_commerce.e_commerce_api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.constant.NameTypeToken;
import com.e_commerce.e_commerce_api.constant.StatusEntity;
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
import com.e_commerce.e_commerce_api.utils.ClientInfo;
import com.e_commerce.e_commerce_api.utils.CookieUtils;
import com.e_commerce.e_commerce_api.utils.DateTimeUtils;
import com.e_commerce.e_commerce_api.utils.DeviceInfoUtils;

import jakarta.servlet.http.HttpServletRequest;
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
    public UserResponse login(LoginRequest requestLogin, String deviceIdClient, HttpServletResponse response, HttpServletRequest request) {
        var user = userRepository.findByEmail(requestLogin.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 1. Kiểm tra khóa tài khoản khi làm bất cứ việc gì
        if (user.getFailedLoginAttempts() >= 5) {
            throw new LoginFailedException(
                    "Account is locked due to too many failed attempts. Please contact support.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            String accessToken = jwtService.generateToken(user, TypeJwt.ACCESS);
            String refreshToken = jwtService.generateToken(user, TypeJwt.REFRESH);

            CookieUtils.addCookie(response, NameTypeToken.accessToken.toString(), accessToken,
                    (int) (accessExpiration / 1000), cookieSecure);
            CookieUtils.addCookie(response, NameTypeToken.refreshToken.toString(), refreshToken,
                    (int) (refreshExpiration / 1000), cookieSecure);
            String deviceId;
            if (deviceIdClient == null) {
                deviceId = UUID.randomUUID().toString();
            } else {
                deviceId = deviceIdClient;
                UserSession oldSession = userSessionRepository.findByDeviceId(deviceId, StatusEntity.ACT.toString())
                        .orElse(null);
                if (oldSession != null) {
                    oldSession.setStatus(StatusEntity.REVOK.toString());
                    oldSession.setRevokedOn(DateTimeUtils.toDateTimeNow());
                    userSessionRepository.save(oldSession);
                }
            }

            UserSession session = UserSession.builder()
                    .user(user)
                    .refreshToken(refreshToken)
                    .sessionToken(accessToken)
                    .deviceId(deviceId)
                    .deviceInfo(DeviceInfoUtils.getDeviceInfo(request.getHeader("User-Agent")))
                    .userAgent(request.getHeader("User-Agent"))
                    .ipAddress(ClientInfo.getClientIp(request))
                    .expiresAt(DateTimeUtils.toLocalDateTime(System.currentTimeMillis() + refreshExpiration))
                    .build();
            userSessionRepository.save(session);

            var userResponse = UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .userName(user.getUsername())
                    .displayName(user.getDisplayName())
                    .status(user.getStatus())
                    .createdOn(user.getCreatedOn())
                    .createdBy(user.getCreatedBy())
                    .modifiedOn(user.getModifiedOn())
                    .modifiedBy(user.getModifiedBy())
                    .avatar(user.getAvatar())
                    .deviceId(deviceId)
                    .build();

            return userResponse;

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
    public void refreshToken(String refreshToken, String deviceIdClient, HttpServletResponse response, HttpServletRequest request) {
        if (refreshToken == null || !jwtService.isTokenValid(refreshToken, TypeJwt.REFRESH)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String email = jwtService.extractSubject(refreshToken, TypeJwt.REFRESH);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        UserSession oldSession = userSessionRepository.findByDeviceId(deviceIdClient, StatusEntity.ACT.toString())
                .orElseThrow(() -> new UnauthorizedException("Token has been revoked or used"));

        if (StatusEntity.REVOK.toString().equals(oldSession.getStatus())) {
            throw new UnauthorizedException("Token has been revoked");
        }

        if (!oldSession.getRefreshToken().toString().equals(refreshToken)) {
            throw new UnauthorizedException("Token not match");
        }

        String newAccessToken = jwtService.generateToken(user, TypeJwt.ACCESS);
        String newRefreshToken = jwtService.generateToken(user, TypeJwt.REFRESH);
        CookieUtils.addCookie(response, NameTypeToken.accessToken.name(), newAccessToken,
                (int) (accessExpiration / 1000), cookieSecure);
        CookieUtils.addCookie(response, NameTypeToken.refreshToken.name(), newRefreshToken,
                (int) (refreshExpiration / 1000), cookieSecure);

        oldSession.setStatus(StatusEntity.REVOK.toString());
        userSessionRepository.save(oldSession);

        UserSession newUsSession = UserSession.builder()
                .user(user)
                .refreshToken(newRefreshToken)
                .sessionToken(newAccessToken)
                .deviceId(deviceIdClient)
                .deviceInfo(oldSession.getDeviceInfo())
                .userAgent(oldSession.getUserAgent())
                .ipAddress(oldSession.getIpAddress())
                .expiresAt(DateTimeUtils.toLocalDateTime(System.currentTimeMillis() + refreshExpiration))
                .build();
        userSessionRepository.save(newUsSession);
    }

    @Transactional
    public void logout(String deviceIdClient, HttpServletResponse response, HttpServletRequest request) {

        UserSession session = userSessionRepository.findByDeviceId(deviceIdClient, StatusEntity.ACT.toString())
                .orElse(null);
        if (session != null) {
            session.setStatus(StatusEntity.REVOK.toString());
            userSessionRepository.save(session);
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
