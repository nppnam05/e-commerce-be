package com.e_commerce.e_commerce_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.e_commerce_api.dto.request.LoginRequest;
import com.e_commerce.e_commerce_api.dto.request.user.CreateUserRequest;
import com.e_commerce.e_commerce_api.dto.response.UserResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @RequestBody LoginRequest request, HttpServletResponse response) {
        var data = authService.login(request, response);
        return ResponseEntity.ok(ApiResponse.success(data, "Success", 200));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(@Valid @RequestBody CreateUserRequest request) {
        var data = authService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Success", 200));
    }

}
