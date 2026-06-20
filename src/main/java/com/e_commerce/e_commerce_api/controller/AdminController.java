package com.e_commerce.e_commerce_api.controller;

import com.e_commerce.e_commerce_api.dto.response.DashboardTotal;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/total-dashboard")
    public ResponseEntity<ApiResponse<DashboardTotal>> getTotal() {
        var data = adminService.getTotal();
        return ResponseEntity.ok(ApiResponse.success(data, "Get total success", 200));
    }
}
