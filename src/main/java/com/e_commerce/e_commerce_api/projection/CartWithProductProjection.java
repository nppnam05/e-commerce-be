package com.e_commerce.e_commerce_api.projection;

import java.math.BigDecimal;

/** CartWithProductProjection */
public interface CartWithProductProjection {
    public Long getId();

    public long getUserId();

    public Long getProductChildrenId();

    public Integer getQuantity();

    public BigDecimal getSinglePrice();

    public String getProductName();

    public String getSize();

    public String getColorCode();

    public String getImageUrls();
}
