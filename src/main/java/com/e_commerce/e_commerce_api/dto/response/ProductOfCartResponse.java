package com.e_commerce.e_commerce_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOfCartResponse {
    String name;
    String size;
    String colorCode;
    BigDecimal price;
    List<String> imageUrls;
}
