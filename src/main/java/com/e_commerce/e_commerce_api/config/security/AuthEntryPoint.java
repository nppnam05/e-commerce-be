package com.e_commerce.e_commerce_api.config.security;

import java.io.IOException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;

import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Void> body = ApiResponse.error("Unauthorized: Token Invalided" + authException.getMessage(), 401,
                null);

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

}