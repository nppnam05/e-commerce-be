package com.e_commerce.e_commerce_api.controller;

import org.springframework.web.bind.annotation.*;

import com.e_commerce.e_commerce_api.dto.response.PaymentLinkResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.PaymentService;
import vn.payos.model.webhooks.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-link/{orderId}")
    public ResponseEntity<ApiResponse<PaymentLinkResponse>> createPaymentLink(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(
                paymentService.createPaymentLink(orderId), "Payment link created", 200));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Webhook webhook) {
        paymentService.handleWebhook(webhook);
        return ResponseEntity.ok("OK");
    }
}