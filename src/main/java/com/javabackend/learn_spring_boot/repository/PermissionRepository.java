package com.javabackend.learn_spring_boot.repository;

import com.javabackend.learn_spring_boot.model.Permission;
import com.javabackend.learn_spring_boot.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
}
