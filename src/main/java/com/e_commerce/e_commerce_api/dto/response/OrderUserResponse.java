package com.e_commerce.e_commerce_api.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderUserResponse {
    private Long id;
    private String code;
    private LocalDateTime createdOn;
    private String status;
    private Integer totalQuantity;
    private BigDecimal totalPrice;
    private String address;
}
