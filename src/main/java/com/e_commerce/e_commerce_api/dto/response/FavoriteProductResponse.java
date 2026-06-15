package com.e_commerce.e_commerce_api.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteProductResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private BigDecimal price;
    private String name;
    private List<String> imageUrls;
}
