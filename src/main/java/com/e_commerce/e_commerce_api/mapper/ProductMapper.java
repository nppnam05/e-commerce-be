package com.e_commerce.e_commerce_api.mapper;

import com.e_commerce.e_commerce_api.dto.request.product.CreateProductRequest;
import com.e_commerce.e_commerce_api.dto.response.ProductResponse;
import com.e_commerce.e_commerce_api.entity.Product;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    Product toEntity(CreateProductRequest request);

    List<ProductResponse> toResponseList(List<Product> products);
}
