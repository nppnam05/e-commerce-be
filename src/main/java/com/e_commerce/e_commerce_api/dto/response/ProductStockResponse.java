package com.e_commerce.e_commerce_api.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductStockResponse {
    private Long id;
    private String name;
    private Integer quantity;
    private String category;
    private String size;
    private String color;
    private BigDecimal price;
    private List<String> imageUrls;
}
