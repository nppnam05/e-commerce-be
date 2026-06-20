package com.e_commerce.e_commerce_api.controller;

import com.e_commerce.e_commerce_api.dto.request.product.CreateProductRequest;
import com.e_commerce.e_commerce_api.dto.request.product.UpdateProductRequest;
import com.e_commerce.e_commerce_api.dto.response.ProductDetailResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.projection.ProductFilterProjection;
import com.e_commerce.e_commerce_api.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity
                .ok(ApiResponse.success(productService.getProducts(keyword, pageNumber, pageSize),
                        "Get products successfully", 200));
    }

    @GetMapping("/filters")
    public ResponseEntity<ApiResponse<List<ProductFilterProjection>>> getProductFilters() {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductFilters(),
                "Get product filters successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id),
                "Get product successfully", 200));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @ModelAttribute CreateProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.createProduct(request),
                "Create product successfully", 200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> updateProduct(@PathVariable Long id,
            @ModelAttribute UpdateProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(id, request),
                "Update product successfully", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.deleteProduct(id),
                "Delete product successfully", 200));
    }
}
