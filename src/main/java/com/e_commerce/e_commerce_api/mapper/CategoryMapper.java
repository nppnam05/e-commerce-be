package com.e_commerce.e_commerce_api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.e_commerce.e_commerce_api.dto.request.category.CreateCategoryRequest;
import com.e_commerce.e_commerce_api.dto.response.CategoryResponse;
import com.e_commerce.e_commerce_api.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CreateCategoryRequest request);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
