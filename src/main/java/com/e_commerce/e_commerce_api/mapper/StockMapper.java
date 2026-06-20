package com.e_commerce.e_commerce_api.mapper;

import com.e_commerce.e_commerce_api.dto.request.stock.CreateStockRequest;
import com.e_commerce.e_commerce_api.dto.response.StockResponse;
import com.e_commerce.e_commerce_api.entity.Stock;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockResponse toResponse(Stock stock);

    Stock toEntity(CreateStockRequest request);

    List<StockResponse> toResponseList(List<Stock> stocks);
}
