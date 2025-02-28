package com.javabackend.learn_spring_boot.service;

import com.javabackend.learn_spring_boot.dto.request.PermissionRequest;
import com.javabackend.learn_spring_boot.dto.response.PermissionResponse;
import com.javabackend.learn_spring_boot.mapper.PermissionMapper;
import com.javabackend.learn_spring_boot.model.Permission;
import com.javabackend.learn_spring_boot.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

//    public List<Permission> getAll(){
//        return permissionRepository.findAll();
//    }
    public List<PermissionResponse> getAll(){
        var permisions = permissionRepository.findAll();
        return permisions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission); //method nay cua spring tra ve
        return permissionMapper.toPermissionResponse(permission);
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
