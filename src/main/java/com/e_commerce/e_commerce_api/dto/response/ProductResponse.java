package com.e_commerce.e_commerce_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse extends BaseResponse {
    private Long id;
    private Long categoryId;
    private Long colorId;
    private Long sizeId;
    private String colorCode;
    private BigDecimal price;
    private String name;
    private String description;
    private List<String> imageUrl;
}
