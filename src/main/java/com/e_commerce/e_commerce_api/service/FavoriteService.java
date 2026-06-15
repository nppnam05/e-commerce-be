package com.e_commerce.e_commerce_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.response.FavoriteProductResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.projection.FavoriteProductProjection;
import com.e_commerce.e_commerce_api.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public PageResponse<List<FavoriteProductResponse>> getAllFavouritesByUserId(Long userId, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        List<FavoriteProductProjection> result = favoriteRepository.findByUserId(userId, pageSize, offset);

        List<FavoriteProductResponse> data = result.stream()
                .map(p -> (FavoriteProductResponse) FavoriteProductResponse.builder()
                        .id(p.getId())
                        .productId(p.getProductId())
                        .name(p.getName())
                        .price(p.getPrice())
                        .imageUrls(p.getImageUrls() != null
                                ? List.of(p.getImageUrls().split(","))
                                : List.of())
                        .build())
                .toList();

        long total = favoriteRepository.countByUserId(userId);

        return PageResponse.mapToPageResponse(data, pageNumber, pageSize, total);
    }

    public Boolean deleteFavorite(Long id){
        var favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
        return true;
    }
}
