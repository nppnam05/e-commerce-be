package com.e_commerce.e_commerce_api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthEntryPoint authEntryPoint;
        private final CustomAccessDeniedHandler accessDeniedHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT không
                                                                                                         // dùng session

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/auth/**").permitAll() // public endpoints
                                                // TODO: Lưu ý, sau chỉ triển khai cái này trên môi trường Local
                                                .requestMatchers(
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/swagger-resources/**",
                                                                "/webjars/**")
                                                .permitAll()
                                                .anyRequest().authenticated()) // còn lại phải có JWT
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(authEntryPoint) // ← 401
                                                .accessDeniedHandler(accessDeniedHandler))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}