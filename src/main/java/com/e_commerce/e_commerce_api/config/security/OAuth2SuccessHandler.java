package com.e_commerce.e_commerce_api.config.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.e_commerce.e_commerce_api.constant.NameTypeToken;
import com.e_commerce.e_commerce_api.constant.StatusEntity;
import com.e_commerce.e_commerce_api.constant.TypeJwt;
import com.e_commerce.e_commerce_api.entity.Role;
import com.e_commerce.e_commerce_api.entity.User;
import com.e_commerce.e_commerce_api.entity.UserSession;
import com.e_commerce.e_commerce_api.repository.RoleRepository;
import com.e_commerce.e_commerce_api.repository.UserRepository;
import com.e_commerce.e_commerce_api.repository.UserSessionRepository;
import com.e_commerce.e_commerce_api.service.JwtService;
import com.e_commerce.e_commerce_api.utils.ClientInfo;
import com.e_commerce.e_commerce_api.utils.CookieUtils;
import com.e_commerce.e_commerce_api.utils.DateTimeUtils;
import com.e_commerce.e_commerce_api.utils.DeviceInfoUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final UserSessionRepository userSessionRepository;
        private final JwtService jwtService;

        @Value("${jwt.access-expiration}")
        private long accessExpiration;

        @Value("${jwt.refresh-expiration}")
        private long refreshExpiration;

        @Value("${app.cookie.secure}")
        private boolean cookieSecure;

        @Value("${app.frontend.url}")
        private String frontendUrl;

        @Override
        @Transactional
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                String email = oauth2User.getAttribute("email");
                String name = oauth2User.getAttribute("name");
                String picture = oauth2User.getAttribute("picture");

                // 1. Tìm hoặc tạo User mới
                User user = userRepository.findByEmail(email).orElseGet(() -> {
                        Role userRole = roleRepository.findByName("USER")
                                        .orElseThrow(() -> new RuntimeException("Default role USER not found"));

                        User newUser = User.builder()
                                        .email(email)
                                        .displayName(name)
                                        .avatar(picture)
                                        .role(userRole)
                                        .passwordHash(null)
                                        .build();
                        return userRepository.save(newUser);
                });

                String deviceId = CookieUtils.getCookieValue(request, "deviceId");

                if (deviceId == null) {
                        deviceId = UUID.randomUUID().toString();
                } else {
                        // Revoke session cũ của device này
                        userSessionRepository.findByDeviceId(deviceId, user, StatusEntity.ACT.toString())
                                        .ifPresent(old -> {
                                                old.setStatus(StatusEntity.REVOK.toString());
                                                old.setRevokedOn(DateTimeUtils.toDateTimeNow());
                                                userSessionRepository.save(old);
                                        });

                }

                // 2. Tạo token và lưu Cookie
                String accessToken = jwtService.generateToken(user, TypeJwt.ACCESS);
                String refreshToken = jwtService.generateToken(user, TypeJwt.REFRESH);

                CookieUtils.addCookie(response, NameTypeToken.accessToken.name(), accessToken,
                                (int) (accessExpiration / 1000),
                                cookieSecure);
                CookieUtils.addCookie(response, NameTypeToken.refreshToken.name(), refreshToken,
                                (int) (refreshExpiration / 1000), cookieSecure);

                // 3. Quản lý Session
                UserSession session = UserSession.builder()
                                .user(user)
                                .refreshToken(refreshToken)
                                .sessionToken(accessToken)
                                .deviceId(deviceId)
                                .deviceInfo(DeviceInfoUtils.parse(request.getHeader("User-Agent")).toString())
                                .userAgent(request.getHeader("User-Agent"))
                                .ipAddress(ClientInfo.getClientIp(request))
                                .expiresAt(DateTimeUtils.toLocalDateTime(
                                                System.currentTimeMillis() + refreshExpiration))
                                .build();
                userSessionRepository.save(session);
                // 4. Redirect về Front-end (trang chủ hoặc trang mong muốn)
                String redirectUrl = frontendUrl + "/home?deviceId=" + deviceId;
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
}
