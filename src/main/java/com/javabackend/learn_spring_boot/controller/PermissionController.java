package com.javabackend.learn_spring_boot.controller;

import com.javabackend.learn_spring_boot.dto.request.ApiResponse;
import com.javabackend.learn_spring_boot.dto.request.PermissionRequest;
import com.javabackend.learn_spring_boot.dto.response.PermissionResponse;
import com.javabackend.learn_spring_boot.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<String> deletePermission(@PathVariable String name){
        permissionService.delete(name);
        return ApiResponse.<String>builder()
                .result("Permission has been delete")
                .build();
    }
}
