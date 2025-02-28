package com.javabackend.learn_spring_boot.service;

import com.javabackend.learn_spring_boot.dto.request.RoleRequest;
import com.javabackend.learn_spring_boot.dto.response.RoleResponse;
import com.javabackend.learn_spring_boot.mapper.RoleMapper;
import com.javabackend.learn_spring_boot.model.Role;
import com.javabackend.learn_spring_boot.repository.PermissionRepository;
import com.javabackend.learn_spring_boot.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
// 2 dong duoi giup ta khoi viet annotation khi su dung cac repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public RoleResponse create(RoleRequest request){
        Role role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public void delete(String name){
        roleRepository.deleteById(name);
    }
}
