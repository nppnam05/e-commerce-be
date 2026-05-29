package com.e_commerce.e_commerce_api.dto.request.product;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Color ID is required")
    private Long colorId;

    @NotNull(message = "Size ID is required")
    private Long sizeId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Images are required")
    private List<MultipartFile> images;
}
