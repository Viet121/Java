package com.javabackend.learn_spring_boot.repository;

import com.javabackend.learn_spring_boot.model.InvalidatedToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends MongoRepository<InvalidatedToken, String> { }
