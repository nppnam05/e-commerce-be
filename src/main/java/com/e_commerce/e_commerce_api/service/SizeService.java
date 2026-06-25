package com.e_commerce.e_commerce_api.service;

import com.e_commerce.e_commerce_api.dto.response.SizeResponse;
import com.e_commerce.e_commerce_api.entity.Size;
import com.e_commerce.e_commerce_api.mapper.SizeMapper;
import com.e_commerce.e_commerce_api.repository.SizeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    public List<SizeResponse> getAllSizes() {
        return sizeMapper.toResponseList(sizeRepository.findAll());
    }

    public List<SizeResponse> getSizesWithStockByProductId(Long productId) {
        List<Size> sizes = sizeRepository.findSizesWithStockByProductId(productId);
        return sizeMapper.toResponseList(sizes);
    }

}
