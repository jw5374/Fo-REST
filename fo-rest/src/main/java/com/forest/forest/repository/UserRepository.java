package com.forest.forest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.forest.forest.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}