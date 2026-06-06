package com.e_commerce.e_commerce_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.response.MonthlyRevenueResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderDetailResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductOrderResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.projection.MonthlyRevenueProjection;
import com.e_commerce.e_commerce_api.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
        private final OrderRepository orderRepository;

        public boolean updateOrderStatus(Long id, String status) {
                var order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
                order.setStatus(status);
                orderRepository.save(order);
                return true;
        }

        public PageResponse<List<OrderResponse>> getOrders(LocalDate dateTime, String status, int pageNumber,
                        int pageSize) {
                int offset = (pageNumber - 1) * pageSize;
                List<OrderResponse> orders = orderRepository.findAllOrder(dateTime, status, pageSize, offset)
                                .stream()
                                .map(p -> OrderResponse.builder()
                                                .id(p.getId())
                                                .status(p.getStatus())
                                                .code(p.getCode())
                                                .createdOn(p.getCreatedOn())
                                                .customerName(p.getCustomerName())
                                                .address(p.getAddress())
                                                .build())
                                .toList();
                long total = orderRepository.countOrders(dateTime, status);
                return PageResponse.mapToPageResponse(orders, pageNumber, pageSize, total);
        }

        public List<MonthlyRevenueResponse> getMonthlyRevenueForThisYear() {
                var monthlyRevenues = orderRepository.getMonthlyRevenueForThisYear();
                var result = MonthlyRevenueResponse.initMonths();
                monthlyRevenues.forEach((value) ->{
                        result.get(value.getMonth() - 1).setRevenue(value.getRevenue());
                });
                return result;
        }

        public OrderDetailResponse getOrderDetail(Long id) {
                var order = orderRepository.findOrderDetailById(id)
                                .orElseThrow(() -> new RuntimeException("Order not found"));
                var products = orderRepository.findProductsByOrderId(id);
                List<ProductOrderResponse> productOrderResponses = products.stream()
                                .map(p -> (ProductOrderResponse) ProductOrderResponse.builder()
                                                .id(p.getId())
                                                .name(p.getName())
                                                .price(p.getPrice())
                                                .quantity(p.getQuantity())
                                                .category(p.getCategory())
                                                .size(p.getSize())
                                                .colorCode(p.getColorCode())
                                                .imageUrls(p.getImageUrls() != null
                                                                ? List.of(p.getImageUrls().split(","))
                                                                : List.of())
                                                .build())
                                .toList();

                return OrderDetailResponse.builder()
                                .id(order.getId())
                                .code(order.getCode())
                                .createdOn(order.getCreatedOn())
                                .status(order.getStatus())
                                .customerName(order.getCustomerName())
                                .address(order.getAddress())
                                .totalAmount(order.getTotalAmount())
                                .products(productOrderResponses)
                                .build();
        }
}
