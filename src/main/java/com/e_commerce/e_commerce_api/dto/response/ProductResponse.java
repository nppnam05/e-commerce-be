package com.e_commerce.e_commerce_api.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProductResponse extends BaseResponse {
    private Long id;
    private Long categoryId;
    private Long colorId;
    private Long sizeId;
    private BigDecimal price;
    private String name;
    private String description;
}
