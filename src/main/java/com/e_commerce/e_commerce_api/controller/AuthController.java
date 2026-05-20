package com.e_commerce.e_commerce_api.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.e_commerce_api.dto.request.auth.LoginRequest;
import com.e_commerce.e_commerce_api.dto.request.user.CreateUserRequest;
import com.e_commerce.e_commerce_api.dto.response.UserResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest requestLogin,
            @CookieValue(name = "deviceId", required = false) String deviceId,
            HttpServletResponse response,
            HttpServletRequest request) {
        var data = authService.login(requestLogin, deviceId, response, request);
        return ResponseEntity.ok(ApiResponse.success(data, "Login successful", 200));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @CookieValue(name = "deviceId", required = false) String deviceId,
            HttpServletResponse response,
            HttpServletRequest request) {
        authService.refreshToken(refreshToken, deviceId, response, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Token refreshed", 200));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "deviceId", required = false) String deviceId,
            HttpServletResponse response,
            HttpServletRequest request) {
        authService.logout(deviceId, response, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful", 200));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(@Valid @RequestBody CreateUserRequest request) {
        var data = authService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Registration successful", 200));
    }
}
