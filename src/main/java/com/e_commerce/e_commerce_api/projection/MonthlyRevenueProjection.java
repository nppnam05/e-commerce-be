package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;

public interface MonthlyRevenueProjection {
    Integer getMonth();

    BigDecimal getRevenue();
}
