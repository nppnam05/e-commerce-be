package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;

public interface ProductChildrenProjection {
    Long getId();

    BigDecimal getPrice();

    String getName();

    String getImageUrls();

    String getCategory();

    Integer getQuantity();

    String getSize();

    String getColorCode();
}
