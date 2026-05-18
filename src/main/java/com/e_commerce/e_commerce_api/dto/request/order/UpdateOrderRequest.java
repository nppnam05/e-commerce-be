package com.e_commerce.e_commerce_api.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
}
