package com.e_commerce.e_commerce_api.mapper;

import com.e_commerce.e_commerce_api.dto.request.order.CreateOrderRequest;
import com.e_commerce.e_commerce_api.dto.response.OrderResponse;
import com.e_commerce.e_commerce_api.entity.Address;
import com.e_commerce.e_commerce_api.entity.Order;
import com.e_commerce.e_commerce_api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(CreateOrderRequest request);

    default OrderResponse toResponse(Order order, Address address, User user) {
        OrderResponse orderResponse = OrderResponse.builder()
                .address(String.format("%s, %s, %s, %s", address.getStreet(), address.getWard(),
                        address.getDistrict(), address.getCity()))
                .status(order.getStatus()).customerName(user.getUsername()).code(order.getCode())
                .build();

        return orderResponse;
    }
}
