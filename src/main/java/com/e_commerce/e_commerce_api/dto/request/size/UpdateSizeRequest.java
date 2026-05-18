package com.e_commerce.e_commerce_api.dto.request.size;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSizeRequest {
    @NotBlank(message = "Size name is required")
    private String name;

    private String description;
}
