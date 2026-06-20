package com.e_commerce.e_commerce_api.service;

import com.e_commerce.e_commerce_api.dto.response.CategoryResponse;
import com.e_commerce.e_commerce_api.mapper.CategoryMapper;
import com.e_commerce.e_commerce_api.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }
}
