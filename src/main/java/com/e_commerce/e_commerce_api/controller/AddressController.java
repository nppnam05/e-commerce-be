package com.e_commerce.e_commerce_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.e_commerce_api.dto.request.address.CreateAddressRequest;
import com.e_commerce.e_commerce_api.dto.request.address.UpdateAddressRequest;
import com.e_commerce.e_commerce_api.dto.response.base.ApiResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.projection.AddressUserProjection;
import com.e_commerce.e_commerce_api.service.AddressService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<List<AddressUserProjection>>>> getAddressesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(ApiResponse.success(
                addressService.getAddressesByUserId(userId, pageNumber, pageSize), "Get addresses successfully", 200));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> createAddress(@PathVariable Long userId,
            @RequestBody CreateAddressRequest request) {
        return ResponseEntity.ok(ApiResponse.success(addressService.createAddress(userId, request),
                "Address created successfully", 200));
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<Boolean>> updateAddress(@RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(ApiResponse.success(addressService.updateAddress(request),
                "Address updated successfully", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(addressService.deleteAddress(id),
                "Address deleted successfully", 200));
    }

}
