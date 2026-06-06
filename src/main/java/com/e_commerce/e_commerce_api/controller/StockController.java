package com.e_commerce.e_commerce_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e_commerce.e_commerce_api.dto.request.stock.UpdateStockRequest;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PutMapping
    public ResponseEntity<ApiResponse<Boolean>> updateStock(
            @RequestBody UpdateStockRequest request) {
        boolean result = stockService.updateStock(request);
        return ResponseEntity.ok(ApiResponse.success(result, "Stock updated successfully", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteStock(@PathVariable Long id) {
        boolean result = stockService.deleteStock(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Stock deleted successfully", 200));
    }
}
