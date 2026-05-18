package com.e_commerce.e_commerce_api.dto.request.color;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateColorRequest {
    @NotBlank(message = "Color name is required")
    private String name;

    @NotBlank(message = "Color code is required")
    private String colorCode;
}
