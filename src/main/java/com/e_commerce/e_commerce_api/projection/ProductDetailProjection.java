package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;

public interface ProductDetailProjection {
    Long getId();

    BigDecimal getPrice();

    String getName();

    String getDescription();

    String getImageUrls();

    Long getCategoryId();

    Long getColorId();

    Long getSizeId();

    String getColorCode();
}
