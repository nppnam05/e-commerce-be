package com.e_commerce.e_commerce_api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.e_commerce.e_commerce_api.dto.request.productChildren.UpdateProductChildren;
import com.e_commerce.e_commerce_api.dto.response.ProductChildrenResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.repository.ColorRepository;
import com.e_commerce.e_commerce_api.repository.ProductChildrenRepository;
import com.e_commerce.e_commerce_api.repository.SizeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductChildrenService {
    private final ProductChildrenRepository productChildrenRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;

    public PageResponse<List<ProductChildrenResponse>> getAllProductChildren(String keyword,
            int pageNumber, int pageSize, Integer productId) {
        int offset = (pageNumber - 1) * pageSize;
        String kw = keyword == null ? "" : keyword;
        var result = productChildrenRepository.findAllProductChildren(kw, pageSize, offset, productId);
        long total = productChildrenRepository.countProductChildren(kw, productId);

        var data = result.stream().map(r -> (ProductChildrenResponse) ProductChildrenResponse.builder()
                .id(r.getId()).name(r.getName()).price(r.getPrice()).colorCode(r.getColorCode())
                .size(r.getSize()).quantity(r.getQuantity()).category(r.getCategory())
                .imageUrls(
                        r.getImageUrls() != null ? List.of(r.getImageUrls().split(",")) : List.of())
                .build()).toList();
        return PageResponse.mapToPageResponse(data, pageNumber, pageSize, total);
    }

    public Boolean updateProductChildren(UpdateProductChildren request) {
        var productChildren = productChildrenRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Product children not found"));
        var size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new RuntimeException("Size not found"));
        var color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found"));
        productChildren.setSize(size);
        productChildren.setColor(color);
        productChildrenRepository.save(productChildren);
        return true;
    }
}
