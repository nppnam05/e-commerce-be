package com.e_commerce.e_commerce_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.constant.TypeJwt;
import com.e_commerce.e_commerce_api.dto.request.LoginRequest;
import com.e_commerce.e_commerce_api.dto.request.user.CreateUserRequest;
import com.e_commerce.e_commerce_api.dto.response.UserResponse;
import com.e_commerce.e_commerce_api.entity.UserSession;
import com.e_commerce.e_commerce_api.exception.BadRequestException;
import com.e_commerce.e_commerce_api.exception.LoginFailedException;
import com.e_commerce.e_commerce_api.exception.NotFoundException;
import com.e_commerce.e_commerce_api.mapper.UserMapper;
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
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${fss.jwt.access-expiration}")
    private long accessExpiration;

    @Value("${fss.jwt.refresh-expiration}")
    private long refreshExpiration;

    @Transactional(dontRollbackOn = LoginFailedException.class)
    public UserResponse login(LoginRequest request, HttpServletResponse response) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            if (user.getFailedLoginAttempts() != 0) {
                user.setFailedLoginAttempts(0);
            }

            String accessToken = jwtService.generateToken(user, TypeJwt.ACCESS);
            String refreshToken = jwtService.generateToken(user, TypeJwt.REFRESH);

            CookieUtils.addCookie(response, "accessToken", accessToken, (int) accessExpiration);
            CookieUtils.addCookie(response, "refreshToken", refreshToken, (int) refreshExpiration);

            UserSession session;
            if (user.getUserSessions().isEmpty()) {
                session = UserSession.builder()
                        .user(user)
                        .refreshToken(refreshToken)
                        .expiresAt(DateTimeUtils.toLocalDateTime(System.currentTimeMillis() + refreshExpiration))
                        .build();
            } else {
                session = user.getUserSessions().get(0);
                session.setRefreshToken(refreshToken);
                session
                        .setExpiresAt(DateTimeUtils.toLocalDateTime(System.currentTimeMillis() + refreshExpiration));
                session.setModifiedBy(user.getUsername());
            }
            userSessionRepository.save(session);
            return userMapper.toResponse(user);
        } catch (BadCredentialsException e) {
            if (user.getFailedLoginAttempts() >= 4) {
                throw new LoginFailedException("Entering the wrong login code more than 5 times");
            }
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            throw new LoginFailedException("Incorrect email or password");
        }
    }

    public UserResponse signUp(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email existed");
        }
        var user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userMapper.toResponse(userRepository.save(user));
    }
}
