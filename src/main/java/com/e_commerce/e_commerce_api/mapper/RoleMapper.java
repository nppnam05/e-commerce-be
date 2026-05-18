package com.e_commerce.e_commerce_api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.e_commerce.e_commerce_api.dto.request.role.CreateRoleRequest;
import com.e_commerce.e_commerce_api.dto.response.RoleResponse;
import com.e_commerce.e_commerce_api.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(Role role);

    Role toEntity(CreateRoleRequest request);

    List<RoleResponse> toResponseList(List<Role> roles);
}
