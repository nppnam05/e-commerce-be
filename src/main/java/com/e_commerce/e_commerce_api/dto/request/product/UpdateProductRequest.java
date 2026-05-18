package com.e_commerce.e_commerce_api.dto.request.product;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
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
}
