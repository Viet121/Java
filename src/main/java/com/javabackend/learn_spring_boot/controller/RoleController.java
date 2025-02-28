package com.javabackend.learn_spring_boot.controller;

import com.javabackend.learn_spring_boot.dto.request.ApiResponse;
import com.javabackend.learn_spring_boot.dto.request.PermissionRequest;
import com.javabackend.learn_spring_boot.dto.request.RoleRequest;
import com.javabackend.learn_spring_boot.dto.response.PermissionResponse;
import com.javabackend.learn_spring_boot.dto.response.RoleResponse;
import com.javabackend.learn_spring_boot.service.PermissionService;
import com.javabackend.learn_spring_boot.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<String> deleteRole(@PathVariable String name){
        roleService.delete(name);
        return ApiResponse.<String>builder()
                .result("Role has been delete")
                .build();
    }
}
