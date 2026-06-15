package com.e_commerce.e_commerce_api.dto.request.user;

import jakarta.validation.constraints.Email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserRequest {
    @Email(message = "Invalid email")
    private String email;
    private String displayName;
    private String phone;
}
