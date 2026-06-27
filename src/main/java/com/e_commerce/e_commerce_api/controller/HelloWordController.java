package com.e_commerce.e_commerce_api.controller;

import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloWordController {
    @GetMapping
    public ResponseEntity<ApiResponse<String>> helloWord() {
        return ResponseEntity.ok(ApiResponse.success("Hello World", "Hello World", 200));
    }
}
