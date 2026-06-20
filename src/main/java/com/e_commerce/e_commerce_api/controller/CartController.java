package com.e_commerce.e_commerce_api.controller;

import com.e_commerce.e_commerce_api.dto.request.cart.CreateCartRequest;
import com.e_commerce.e_commerce_api.dto.request.cart.UpdateCartRequest;
import com.e_commerce.e_commerce_api.dto.response.CartResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartResponse>>> getCartsByUserId(
            @PathVariable long userId) {
        return ResponseEntity.ok(ApiResponse.success(cartService.getAllCartByUserId(userId),
                "Get all carts success", 200));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<CartResponse>> createCart(
            @RequestBody CreateCartRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(cartService.createCart(request), "Create cart success", 200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> updateCart(@PathVariable long id,
            @RequestBody UpdateCartRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cartService.updateCart(id, request),
                "Update cart success", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCart(@PathVariable long id) {
        return ResponseEntity
                .ok(ApiResponse.success(cartService.deleteCart(id), "Delete cart success", 200));
    }
}
