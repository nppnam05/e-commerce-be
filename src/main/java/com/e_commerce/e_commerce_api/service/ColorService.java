package com.e_commerce.e_commerce_api.service;

import com.e_commerce.e_commerce_api.dto.response.ColorResponse;
import com.e_commerce.e_commerce_api.entity.Color;
import com.e_commerce.e_commerce_api.mapper.ColorMapper;
import com.e_commerce.e_commerce_api.repository.ColorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public List<ColorResponse> getAllColors() {
        return colorMapper.toResponseList(colorRepository.findAll());
    }

    public List<ColorResponse> getColorsWithStockByProductId(Long productId) {
        List<Color> colors = colorRepository.findColorsWithStockByProductId(productId);
        return colorMapper.toResponseList(colors);
    }

}
