package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderUserProjection {
    Long getId();

    String getCode();

    String getStatus();

    LocalDateTime getCreatedOn();

    Integer getTotalQuantity();

    BigDecimal getTotalPrice();

    String getStreet();

    String getWard();

    String getDistrict();

    String getCity();
}
