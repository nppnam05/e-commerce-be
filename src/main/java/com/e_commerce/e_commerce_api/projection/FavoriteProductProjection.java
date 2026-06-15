package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;


public interface FavoriteProductProjection {
    Long getId();

    Long getUserId();

    Long getProductId();

    BigDecimal getPrice();

    String getName();

    String getImageUrls();
}
