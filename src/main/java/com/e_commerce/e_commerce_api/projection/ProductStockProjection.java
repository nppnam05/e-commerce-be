package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;

public interface ProductStockProjection {
    Long getId();

    BigDecimal getPrice();

    String getName();

    String getImageUrls();

    String getCategory();

    Integer getQuantity();

    String getSize();

    String getColor();
}
