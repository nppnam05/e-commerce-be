package com.e_commerce.e_commerce_api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.e_commerce.e_commerce_api.dto.request.cart.CreateCartRequest;
import com.e_commerce.e_commerce_api.dto.response.CartResponse;
import com.e_commerce.e_commerce_api.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponse toResponse(Cart cart);

    Cart toEntity(CreateCartRequest request);

    List<CartResponse> toResponseList(List<Cart> carts);
}
