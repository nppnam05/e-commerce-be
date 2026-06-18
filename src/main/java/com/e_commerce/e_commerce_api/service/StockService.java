package com.e_commerce.e_commerce_api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.e_commerce.e_commerce_api.dto.request.stock.UpdateStockRequest;
import com.e_commerce.e_commerce_api.dto.response.ProductStockResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public PageResponse<List<ProductStockResponse>> getAllProductsStock(String keyword,
            int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String kw = keyword == null ? "" : keyword;
        var result = stockRepository.findAllStocks(kw, pageSize, offset);
        long total = stockRepository.countStocks(kw);

        var data = result.stream().map(r -> (ProductStockResponse) ProductStockResponse.builder()
                .id(r.getId()).name(r.getName()).price(r.getPrice()).colorCode(r.getColorCode())
                .size(r.getSize()).quantity(r.getQuantity()).category(r.getCategory())
                .imageUrls(
                        r.getImageUrls() != null ? List.of(r.getImageUrls().split(",")) : List.of())
                .build()).toList();
        return PageResponse.mapToPageResponse(data, pageNumber, pageSize, total);
    }

    public boolean updateStock(UpdateStockRequest request) {
        var stock = stockRepository.findById(request.getId())
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
