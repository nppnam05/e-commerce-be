package com.e_commerce.e_commerce_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.constant.PaymentStatus;
import com.e_commerce.e_commerce_api.dto.response.PaymentLinkResponse;
import com.e_commerce.e_commerce_api.entity.Order;
import com.e_commerce.e_commerce_api.exception.BadRequestException;
import com.e_commerce.e_commerce_api.exception.NotFoundException;
import com.e_commerce.e_commerce_api.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.webhooks.Webhook;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PayOS payOS;
    private final OrderRepository orderRepository;

    @Value("${app.cors.allowed-origins}")
    private String frontendUrl;

    public PaymentLinkResponse createPaymentLink(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (PaymentStatus.PAID.name().equals(order.getPaymentStatus())) {
            throw new BadRequestException("Order has already been paid");
        }

        var request = CreatePaymentLinkRequest.builder().orderCode(order.getId())
                .amount(order.getTotalPrice().longValue())
                .description("Thanh toan " + order.getCode())
                .cancelUrl(frontendUrl + "/payment/cancel")
                .returnUrl(frontendUrl + "/payment/success").build();

        var paymentLink = payOS.paymentRequests().create(request);

        order.setPaymentLinkId(paymentLink.getPaymentLinkId());
        order.setPaymentStatus(PaymentStatus.PND.name());
        orderRepository.save(order);

        return PaymentLinkResponse.builder().qrCode(paymentLink.getQrCode())
                .checkoutUrl(paymentLink.getCheckoutUrl())
                .paymentLinkId(paymentLink.getPaymentLinkId()).build();
    }

    public void handleWebhook(Webhook webhook) {
        var data = payOS.webhooks().verify(webhook);

        Long orderId = data.getOrderCode();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if ("00".equals(data.getCode())) {
            order.setPaymentStatus(PaymentStatus.PAID.name());
            orderRepository.save(order);
        }
    }
}
