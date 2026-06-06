package com.e_commerce.e_commerce_api.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ProductOrderResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private String size;
    private String colorCode;
    private List<String> imageUrls;
}
