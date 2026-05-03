package com.e_commerce.e_commerce_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.e_commerce.e_commerce_api.dto.request.user.CreateUserRequest;
import com.e_commerce.e_commerce_api.dto.response.UserResponse;
import com.e_commerce.e_commerce_api.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    User toEntity(CreateUserRequest request);

    List<UserResponse> toResponseList(List<User> users);
}
