package com.e_commerce.e_commerce_api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.e_commerce.e_commerce_api.dto.request.color.CreateColorRequest;
import com.e_commerce.e_commerce_api.dto.response.ColorResponse;
import com.e_commerce.e_commerce_api.entity.Color;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    ColorResponse toResponse(Color color);

    Color toEntity(CreateColorRequest request);

    List<ColorResponse> toResponseList(List<Color> colors);
}
