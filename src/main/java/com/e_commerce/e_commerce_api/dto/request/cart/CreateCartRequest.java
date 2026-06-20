package com.e_commerce.e_commerce_api.dto.request.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product Children ID is required")
    private Long productChildrenId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
