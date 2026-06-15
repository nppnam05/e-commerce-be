package com.e_commerce.e_commerce_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.request.address.CreateAddressRequest;
import com.e_commerce.e_commerce_api.dto.request.address.UpdateAddressRequest;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.entity.Address;
import com.e_commerce.e_commerce_api.projection.AddressUserProjection;
import com.e_commerce.e_commerce_api.repository.AddressRepository;
import com.e_commerce.e_commerce_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public PageResponse<List<AddressUserProjection>> getAddressesByUserId(Long userId, int pageNumber, int pageSize) {
        long total = addressRepository.countByUserId(userId);
        int offset = (pageNumber - 1) * pageSize;
        return PageResponse.mapToPageResponse(addressRepository.findByUserId(userId, pageSize, offset), pageNumber,
                pageSize, total);
    }

    public Boolean createAddress(Long userId, CreateAddressRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var address = Address.builder()
                .isDefault(request.getIsDefault())
                .street(request.getStreet())
                .district(request.getDistrict())
                .ward(request.getWard())
                .city(request.getCity())
                .user(user)
                .build();
        addressRepository.save(address);
        return true;
    }

    public Boolean updateAddress(UpdateAddressRequest request) {
        var address = addressRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        address.setIsDefault(request.getIsDefault());
        address.setStreet(request.getStreet());
        address.setDistrict(request.getDistrict());
        address.setWard(request.getWard());
        address.setCity(request.getCity());
        addressRepository.save(address);
        return true;
    }

    public Boolean deleteAddress(Long id) {
        var address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
        return true;
    }
}
