package com.e_commerce.e_commerce_api.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
}
