package com.e_commerce.e_commerce_api.service;

import com.e_commerce.e_commerce_api.constant.PaymentStatus;
import com.e_commerce.e_commerce_api.dto.request.order.CreateOrderRequest;
import com.e_commerce.e_commerce_api.dto.response.MonthlyRevenueResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderDetailResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderResponse;
import com.e_commerce.e_commerce_api.dto.response.OrderUserResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductOrderResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.entity.Cart;
import com.e_commerce.e_commerce_api.entity.Order;
import com.e_commerce.e_commerce_api.entity.OrderProduct;
import com.e_commerce.e_commerce_api.exception.NotFoundException;
import com.e_commerce.e_commerce_api.mapper.OrderMapper;
import com.e_commerce.e_commerce_api.repository.AddressRepository;
import com.e_commerce.e_commerce_api.repository.CartRepository;
import com.e_commerce.e_commerce_api.repository.OrderRepository;
import com.e_commerce.e_commerce_api.utils.ExceptionGenerator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
        private final OrderRepository orderRepository;
        private final CartRepository cartRepository;
        private final AddressRepository addressRepository;
        private final OrderMapper orderMapper;

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
                                                                p.getStreet(), p.getWard(), p.getDistrict(),
                                                                p.getCity()))
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
                                                                p.getStreet(), p.getWard(), p.getDistrict(),
                                                                p.getCity()))
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

        @Transactional
        public OrderResponse createOrder(CreateOrderRequest request) {
                List<Cart> carts = cartRepository.findByUserIdWithDetail(request.getUserId());

                if (carts.isEmpty()) {
                        throw new NotFoundException("User don't have any item on cart");
                }

                var totalPrice = carts.stream()
                                .map(cart -> cart.getSinglePrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                var totalQuantity = carts.stream().mapToInt(Cart::getQuantity).sum();
                var user = carts.stream().findFirst().get().getUser();
                var address = addressRepository.findById(request.getAddressId()).orElseThrow(
                                () -> ExceptionGenerator.handleNotFoundAddress(request.getAddressId()));

                String code = "#OR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

                var order = Order.builder().user(user).address(address).code(code)
                                .status(PaymentStatus.PND.name()).paymentStatus(PaymentStatus.PND.name())
                                .totalQuantity(totalQuantity).totalPrice(totalPrice).build();

                Order saveOrder = orderRepository.save(order);

                List<OrderProduct> orderProducts = carts.stream()
                                .map(cart -> OrderProduct.builder().quantity(cart.getQuantity())
                                                .singlePrice(cart.getSinglePrice()).order(saveOrder)
                                                .productChildren(cart.getProductChildren()).build())
                                .collect(Collectors.toList());

                cartRepository.deleteAll(carts);
                saveOrder.setOrderProducts(orderProducts);

                return orderMapper.toResponse(saveOrder, address, user);
        }

        public OrderDetailResponse getOrderDetail(Long id) {
                var order = orderRepository.findOrderDetailById(id)
                                .orElseThrow(() -> new RuntimeException("Order not found"));
                var products = orderRepository.findProductsByOrderId(id);
                List<ProductOrderResponse> productOrderResponses = products.stream()
                                .map(p -> (ProductOrderResponse) ProductOrderResponse.builder().id(p.getId())
                                                .name(p.getName()).price(p.getPrice()).quantity(p.getQuantity())
                                                .category(p.getCategory()).size(p.getSize()).colorCode(p.getColorCode())
                                                .imageUrls(p.getImageUrls() != null
                                                                ? List.of(p.getImageUrls().split(","))
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
