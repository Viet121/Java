package com.javabackend.learn_spring_boot.repository;

import com.javabackend.learn_spring_boot.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
