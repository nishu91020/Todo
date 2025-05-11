package com.example.todoApplication.repository;

import com.example.todoApplication.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<User,String> {
    // find user by id
    Optional<User> findByUsername(String username);
}
