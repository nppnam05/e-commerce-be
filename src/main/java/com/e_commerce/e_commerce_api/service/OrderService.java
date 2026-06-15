package com.e_commerce.e_commerce_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.response.MonthlyRevenueResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderDetailResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderUserResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductOrderResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public boolean updateOrderStatus(Long id, String status) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }

    public PageResponse<List<OrderUserResponse>> getOrdersByUserId(Long userId, int pageNumber,
            int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        var result = orderRepository.findOrdersByUserId(userId, pageSize, offset);
        var orders = result.stream()
                .map(p -> (OrderUserResponse) OrderUserResponse.builder().id(p.getId())
                        .status(p.getStatus()).code(p.getCode()).createdOn(p.getCreatedOn())
                        .totalPrice(p.getTotalPrice()).totalQuantity(p.getTotalQuantity())
                        .address("Đường %s, Phường %s, Quận %s, Thành phố %s".formatted(
                                p.getStreet(), p.getWard(), p.getDistrict(), p.getCity()))
                        .build())
                .toList();
        long total = orderRepository.countOrdersByUserId(userId);
        return PageResponse.mapToPageResponse(orders, pageNumber, pageSize, total);
    }

    public PageResponse<List<OrderResponse>> getOrders(LocalDate dateTime, String status,
            int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        List<OrderResponse> orders = orderRepository
                .findAllOrder(dateTime, status, pageSize, offset).stream()
                .map(p -> OrderResponse.builder().id(p.getId()).status(p.getStatus())
                        .code(p.getCode()).createdOn(p.getCreatedOn())
                        .customerName(p.getCustomerName())
                        .address("Đường %s, Phường %s, Quận %s, Thành phố %s".formatted(
                                p.getStreet(), p.getWard(), p.getDistrict(), p.getCity()))
                        .build())
                .toList();
        long total = orderRepository.countOrders(dateTime, status);
        return PageResponse.mapToPageResponse(orders, pageNumber, pageSize, total);
    }

    public List<MonthlyRevenueResponse> getMonthlyRevenueForThisYear() {
        var monthlyRevenues = orderRepository.getMonthlyRevenueForThisYear();
        var result = MonthlyRevenueResponse.initMonths();
        monthlyRevenues.forEach((value) -> {
            result.get(value.getMonth() - 1).setRevenue(value.getRevenue());
        });
        return result;
    }

    public OrderDetailResponse getOrderDetail(Long id) {
        var order = orderRepository.findOrderDetailById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        var products = orderRepository.findProductsByOrderId(id);
        List<ProductOrderResponse> productOrderResponses = products.stream()
                .map(p -> (ProductOrderResponse) ProductOrderResponse.builder().id(p.getId())
                        .name(p.getName()).price(p.getPrice()).quantity(p.getQuantity())
                        .category(p.getCategory()).size(p.getSize()).colorCode(p.getColorCode())
                        .imageUrls(p.getImageUrls() != null ? List.of(p.getImageUrls().split(","))
                                : List.of())
                        .build())
                .toList();

        return OrderDetailResponse.builder().id(order.getId()).code(order.getCode())
                .createdOn(order.getCreatedOn()).status(order.getStatus())
                .customerName(order.getCustomerName())
                .address("Đường %s, Phường %s, Quận %s, Thành phố %s".formatted(order.getStreet(),
                        order.getWard(), order.getDistrict(), order.getCity()))
                .totalAmount(order.getTotalAmount()).products(productOrderResponses).build();
    }
}
