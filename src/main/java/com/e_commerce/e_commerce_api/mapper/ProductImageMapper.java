package com.e_commerce.e_commerce_api.mapper;

import com.e_commerce.e_commerce_api.dto.request.productimage.CreateProductImageRequest;
import com.e_commerce.e_commerce_api.dto.response.ProductImageResponse;
import com.e_commerce.e_commerce_api.entity.ProductImage;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    ProductImageResponse toResponse(ProductImage productImage);

    ProductImage toEntity(CreateProductImageRequest request);

    List<ProductImageResponse> toResponseList(List<ProductImage> productImages);
}
