package com.javabackend.learn_spring_boot.mapper;

import com.javabackend.learn_spring_boot.dto.request.RoleRequest;
import com.javabackend.learn_spring_boot.dto.response.PermissionResponse;
import com.javabackend.learn_spring_boot.dto.response.RoleResponse;
import com.javabackend.learn_spring_boot.model.Permission;
import com.javabackend.learn_spring_boot.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // khong map cai permissions vo, ta se tu map bang tay
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
