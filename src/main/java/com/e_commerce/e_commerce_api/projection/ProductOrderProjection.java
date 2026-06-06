package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;

public interface ProductOrderProjection {
    Long getId();
    BigDecimal getPrice();
    Integer getQuantity();
    String getName();
    String getImageUrls();
    String getCategory();
    String getSize();
    String getColorCode();
}
