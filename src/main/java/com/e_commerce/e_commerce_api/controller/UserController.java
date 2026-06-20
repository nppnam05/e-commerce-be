package com.e_commerce.e_commerce_api.controller;

import com.e_commerce.e_commerce_api.dto.request.user.UpdateUserRequest;
import com.e_commerce.e_commerce_api.dto.response.UserResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.JwtService;
import com.e_commerce.e_commerce_api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(HttpServletRequest request) {

        var data = userService.getMe(jwtService.extractUserIdFromCookie(request));
        return ResponseEntity.ok(ApiResponse.success(data, "Get me success", 200));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> updateProfile(@PathVariable String id,
            @RequestBody UpdateUserRequest request) {
        var result = userService.updateProfile(Long.parseLong(id), request);
        return ResponseEntity.ok(ApiResponse.success(result, "Update profile success", 200));
    }
}
