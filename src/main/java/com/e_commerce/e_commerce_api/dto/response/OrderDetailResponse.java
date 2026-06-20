package com.e_commerce.e_commerce_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class OrderDetailResponse {
    private Long id;
    private String code;
    private LocalDateTime createdOn;
    private String status;
    private String customerName;
    private String address;
    private BigDecimal totalAmount;
    private List<ProductOrderResponse> products;
}
