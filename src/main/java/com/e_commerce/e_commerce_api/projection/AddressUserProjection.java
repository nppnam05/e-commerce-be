package com.e_commerce.e_commerce_api.projection;

public interface AddressUserProjection {
    Long getId();

    Long getUserId();

    String getStreet();

    String getCity();

    String getDistrict();

    String getWard();

    Boolean getIsDefault();
}
