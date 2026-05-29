package com.e_commerce.e_commerce_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.e_commerce_api.dto.response.ColorResponse;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.service.ColorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ColorResponse>>> getAllColors() {
        return ResponseEntity.ok(ApiResponse.success(
                colorService.getAllColors(),
                "Get all colors successfully",
                200
        ));
    }
}
