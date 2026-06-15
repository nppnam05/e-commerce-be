package com.e_commerce.e_commerce_api.service;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.response.DashboardTotal;
import com.e_commerce.e_commerce_api.projection.TotalProjection;
import com.e_commerce.e_commerce_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public DashboardTotal getTotal() {
        TotalProjection data = userRepository.getTotal();
        return DashboardTotal.builder().totalUsers(data.getTotalUsers())
                .totalPending(data.getTotalPending()).totalSales(data.getTotalSales())
                .totalOrders(data.getTotalOrders()).build();
    }
}
