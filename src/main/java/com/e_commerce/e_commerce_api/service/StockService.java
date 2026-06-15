package com.e_commerce.e_commerce_api.service;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.request.stock.UpdateStockRequest;
import com.e_commerce.e_commerce_api.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public boolean updateStock(UpdateStockRequest request) {
        var stock = stockRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stock.setQuantity(request.getQuantity());
        stockRepository.save(stock);
        return true;
    }

    public boolean deleteStock(Long id) {
        stockRepository.deleteById(id);
        return true;
    }
}
