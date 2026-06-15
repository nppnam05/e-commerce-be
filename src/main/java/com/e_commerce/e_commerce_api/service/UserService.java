package com.e_commerce.e_commerce_api.service;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.request.user.UpdateUserRequest;
import com.e_commerce.e_commerce_api.dto.response.UserResponse;
import com.e_commerce.e_commerce_api.exception.NotFoundException;
import com.e_commerce.e_commerce_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getMe(Long id) {
        var user = userRepository.findByIdWithRole(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        var userResponse = UserResponse.builder().id(user.getId())
                .roleName(user.getRole().getName()).phone(user.getPhone()).email(user.getEmail())
                .displayName(user.getDisplayName()).status(user.getStatus())
                .createdOn(user.getCreatedOn()).createdBy(user.getCreatedBy())
                .modifiedOn(user.getModifiedOn()).modifiedBy(user.getModifiedBy())
                .avatar(user.getAvatar()).build();

        return userResponse;
    }

    public boolean updateProfile(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (request.getEmail() != null && !user.getEmail().equals(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);
        return true;
    }
}
