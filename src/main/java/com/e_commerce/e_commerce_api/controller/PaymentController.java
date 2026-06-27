package com.e_commerce.e_commerce_api.controller;

import org.springframework.web.bind.annotation.*;

import com.e_commerce.e_commerce_api.dto.response.PaymentLinkResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.PaymentService;
import vn.payos.model.webhooks.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-link/{orderId}")
    public ResponseEntity<ApiResponse<PaymentLinkResponse>> createPaymentLink(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.createPaymentLink(orderId),
                "Payment link created", 200));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Webhook webhook) {
        try {
            paymentService.handleWebhook(webhook);
        } catch (Exception e) {
            log.error("Webhook error: {}", e.getMessage());
        }
        return ResponseEntity.ok("OK");
    }
}
