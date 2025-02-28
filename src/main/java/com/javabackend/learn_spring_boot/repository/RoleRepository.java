package com.javabackend.learn_spring_boot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.javabackend.learn_spring_boot.model.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {}
