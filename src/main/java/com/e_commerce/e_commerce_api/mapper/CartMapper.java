package com.e_commerce.e_commerce_api.mapper;

import com.e_commerce.e_commerce_api.dto.response.CartResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductOfCartResponse;
import com.e_commerce.e_commerce_api.entity.Cart;
import com.e_commerce.e_commerce_api.entity.Product;
import com.e_commerce.e_commerce_api.entity.ProductChildren;
import com.e_commerce.e_commerce_api.projection.CartWithProductProjection;
import java.util.Arrays;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "product", ignore = true)
    CartResponse toResponse(Cart cart);

    List<CartResponse> toResponseList(List<Cart> carts);

    @Mapping(target = "product", ignore = true)
    CartResponse toResponse(CartWithProductProjection cart);

    default ProductOfCartResponse toProductResponse(CartWithProductProjection cart) {
        List<String> images = Arrays.asList(cart.getImageUrls().split(", "));

        ProductOfCartResponse response = ProductOfCartResponse.builder().name(cart.getProductName())
                .size(cart.getSize()).colorCode(cart.getColorCode()).price(cart.getSinglePrice())
                .imageUrls(images).build();

        return response;
    }

    default ProductOfCartResponse toProductResponse(ProductChildren productChildren) {
        Product product = productChildren.getProduct();
        List<String> imageUrls = product.getProductImages().stream()
                .map(productImages -> productImages.getUrl()).toList();

        ProductOfCartResponse response = ProductOfCartResponse.builder().name(product.getName())
                .size(productChildren.getSize().getName())
                .colorCode(productChildren.getColor().getName()).price(product.getPrice())
                .imageUrls(imageUrls).build();

        return response;
    }
}
