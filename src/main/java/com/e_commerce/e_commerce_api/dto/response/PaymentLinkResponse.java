package com.e_commerce.e_commerce_api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentLinkResponse {
    private String qrCode;
    private String checkoutUrl;
    private String paymentLinkId;
}