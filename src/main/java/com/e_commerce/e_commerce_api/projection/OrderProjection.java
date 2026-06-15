package com.e_commerce.e_commerce_api.projection;

import java.time.LocalDateTime;

public interface OrderProjection {
    Long getId();

    String getStatus();

    String getCustomerName();

    String getWard();

    String getDistrict();

    String getStreet();

    String getCity();

    String getCode();

    LocalDateTime getCreatedOn();
}
