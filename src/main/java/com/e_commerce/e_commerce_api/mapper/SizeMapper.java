package com.e_commerce.e_commerce_api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.e_commerce.e_commerce_api.dto.request.size.CreateSizeRequest;
import com.e_commerce.e_commerce_api.dto.response.SizeResponse;
import com.e_commerce.e_commerce_api.entity.Size;

@Mapper(componentModel = "spring")
public interface SizeMapper {

    SizeResponse toResponse(Size size);

    Size toEntity(CreateSizeRequest request);

    List<SizeResponse> toResponseList(List<Size> sizes);
}
