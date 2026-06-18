package com.e_commerce.e_commerce_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.e_commerce.e_commerce_api.dto.request.productChildren.UpdateProductChildren;
import com.e_commerce.e_commerce_api.dto.response.ProductChildrenResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.service.ProductChildrenService;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product-children")
@RequiredArgsConstructor
public class ProductChildrenController {

    private final ProductChildrenService productChildrenService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<ProductChildrenResponse>>>> getAllProductChildren(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer productId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(ApiResponse.success(
                productChildrenService.getAllProductChildren(keyword, pageNumber, pageSize, productId),
                "Get product children successfully", 200));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Boolean>> updateProductChildren(
            @RequestBody UpdateProductChildren request) {
        return ResponseEntity
                .ok(ApiResponse.success(productChildrenService.updateProductChildren(request),
                        "Update product children successfully", 200));
    }
}
