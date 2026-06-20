package com.e_commerce.e_commerce_api.dto.request.productChildren;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductChildren {
    @NotNull(message = "Product Children ID is required")
    private Long id;

    @NotNull(message = "Size ID is required")
    private Long sizeId;

    @NotNull(message = "Color ID is required")
    private Long colorId;
}
