package com.e_commerce.e_commerce_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardTotal {
    private Long totalUsers;
    private Long totalPending;
    private Long totalSales;
    private Long totalOrders;
}
