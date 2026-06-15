package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderDetailProjection {
    Long getId();

    String getStatus();

    String getCustomerName();

    String getStreet();

    String getWard();

    String getDistrict();

    String getCity();

    String getCode();

    LocalDateTime getCreatedOn();

    BigDecimal getTotalAmount();
}
