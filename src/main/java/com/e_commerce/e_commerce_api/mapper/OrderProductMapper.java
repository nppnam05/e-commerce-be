package com.e_commerce.e_commerce_api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.e_commerce.e_commerce_api.dto.request.orderproduct.CreateOrderProductRequest;
import com.e_commerce.e_commerce_api.dto.response.OrderProductResponse;
import com.e_commerce.e_commerce_api.entity.OrderProduct;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {

    OrderProductResponse toResponse(OrderProduct orderProduct);

    OrderProduct toEntity(CreateOrderProductRequest request);

    List<OrderProductResponse> toResponseList(List<OrderProduct> orderProducts);
}
