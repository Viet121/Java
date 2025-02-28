package com.javabackend.learn_spring_boot.mapper;

import com.javabackend.learn_spring_boot.dto.request.PermissionRequest;
import com.javabackend.learn_spring_boot.dto.request.UserCreatRequest;
import com.javabackend.learn_spring_boot.dto.request.UserUpdateRequest;
import com.javabackend.learn_spring_boot.dto.response.PermissionResponse;
import com.javabackend.learn_spring_boot.dto.response.UserResponse;
import com.javabackend.learn_spring_boot.model.Permission;
import com.javabackend.learn_spring_boot.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    // Method nay tu dong tao moi mot Permission va tra ve mot Permission voi field tu PermissionRequest
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    // Method nay khong tao va tra ve Permission, chi tu set() du lieu
    void updatePermission(@MappingTarget Permission permission, PermissionRequest request);

}
