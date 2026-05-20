package com.e_commerce.e_commerce_api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.e_commerce.e_commerce_api.constant.NameTypeToken;
import com.e_commerce.e_commerce_api.constant.StatusEntity;
import com.e_commerce.e_commerce_api.constant.TypeJwt;
import com.e_commerce.e_commerce_api.entity.UserSession;
import com.e_commerce.e_commerce_api.repository.UserSessionRepository;
import com.e_commerce.e_commerce_api.service.JwtService;
import com.e_commerce.e_commerce_api.utils.CookieUtils;
import com.e_commerce.e_commerce_api.utils.DateTimeUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserSessionRepository userSessionRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        final String email;

        // Ưu tiên Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            // Fallback: đọc từ cookie
            jwt = CookieUtils.getCookieValue(request, NameTypeToken.accessToken.name());
        }

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Trích xuất email
        email = jwtService.extractSubject(jwt, TypeJwt.ACCESS);

        // 3. Nếu có email và chưa được xác thực trong SecurityContext
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(jwt, TypeJwt.ACCESS)) {
                // check session trong DB
                UserSession session = userSessionRepository.findBySessionToken(jwt).orElse(null);
                if (session != null && StatusEntity.ACT.toString().equals(session.getStatus())) {
                    // Tạo đối tượng Authentication để báo cho Spring biết User này đã hợp lệ
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Lưu vào Context của hệ thống
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // cập nhật LastAccessedOn nếu đã quá 5 phút
                    boolean shouldUpdate = session.getLastAccessedOn() == null ||
                            session.getLastAccessedOn().isBefore(DateTimeUtils.toDateTimeNow().minusMinutes(5));

                    if (shouldUpdate) {
                        session.setLastAccessedOn(DateTimeUtils.toDateTimeNow());
                        userSessionRepository.save(session);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/")
                || path.startsWith("/login/oauth2/");
    }
}