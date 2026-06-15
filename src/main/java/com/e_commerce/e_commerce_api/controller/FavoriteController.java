package com.e_commerce.e_commerce_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.dto.response.FavoriteProductResponse;
import com.e_commerce.e_commerce_api.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<List<FavoriteProductResponse>>>> getFavouritesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity
                .ok(ApiResponse.success(favoriteService.getAllFavouritesByUserId(userId, pageNumber, pageSize),
                        "Get favorites successfully", 200));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(favoriteService.deleteFavorite(id),
                "Delete favorite successfully", 200));
    }
}
