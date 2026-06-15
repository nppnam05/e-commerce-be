package com.e_commerce.e_commerce_api.mapper;

import org.mapstruct.Mapper;
import com.e_commerce.e_commerce_api.dto.request.order.CreateOrderRequest;
import com.e_commerce.e_commerce_api.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(CreateOrderRequest request);
}
