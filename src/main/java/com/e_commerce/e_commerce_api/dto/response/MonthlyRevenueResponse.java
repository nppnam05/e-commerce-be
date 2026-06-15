package com.e_commerce.e_commerce_api.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MonthlyRevenueResponse {
    private String month;
    private BigDecimal revenue;

    public static List<MonthlyRevenueResponse> initMonths() {
        return List.of(
                MonthlyRevenueResponse.builder().month("Tháng 1").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 2").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 3").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 4").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 5").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 6").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 7").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 8").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 9").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 10").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 11").revenue(BigDecimal.ZERO).build(),
                MonthlyRevenueResponse.builder().month("Tháng 12").revenue(BigDecimal.ZERO)
                        .build());
    }
}
