package com.e_commerce.e_commerce_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e_commerce.e_commerce_api.dto.request.product.CreateProductRequest;
import com.e_commerce.e_commerce_api.dto.response.ProductResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<ProductResponse>>>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProducts(keyword, pageNumber, pageSize),
                "Get products successfully", 200));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @ModelAttribute CreateProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.createProduct(request), "Create product successfully",
                200));
    }
}
