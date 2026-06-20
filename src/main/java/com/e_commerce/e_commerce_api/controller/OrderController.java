package com.e_commerce.e_commerce_api.controller;

import com.e_commerce.e_commerce_api.dto.request.order.CreateOrderRequest;
import com.e_commerce.e_commerce_api.dto.response.MonthlyRevenueResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderDetailResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderUserResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.service.OrderService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<OrderResponse>>>> getOrders(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTime,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(
                ApiResponse.success(orderService.getOrders(dateTime, status, pageNumber, pageSize),
                        "Get orders successfully", 200));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<List<OrderUserResponse>>>> getUserOrders(
            @PathVariable Long userId, @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(
                ApiResponse.success(orderService.getOrdersByUserId(userId, pageNumber, pageSize),
                        "Get user orders successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderDetail(id),
                "Get order detail successfully", 200));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Boolean>> updateOrderStatus(@PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success(orderService.updateOrderStatus(id, status),
                "Order status updated successfully", 200));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(orderService.createOrder(request),
                "Create order success", 200));
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<ApiResponse<List<MonthlyRevenueResponse>>> getMonthlyRevenueForThisYear() {
        return ResponseEntity.ok(ApiResponse.success(orderService.getMonthlyRevenueForThisYear(),
                "Get monthly revenue for this year successfully", 200));
    }
}
