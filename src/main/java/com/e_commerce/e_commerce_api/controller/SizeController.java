package com.e_commerce.e_commerce_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.e_commerce_api.dto.response.SizeResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.SizeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/size")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SizeResponse>>> getAllSizes() {
        return ResponseEntity.ok(
                ApiResponse.success(sizeService.getAllSizes(), "Get all sizes successfully", 200));
    }
}
