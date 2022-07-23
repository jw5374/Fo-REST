package com.forest.forest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.forest.forest.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "UPDATE users SET shippingaddress = :address WHERE username = :username RETURNING users.shippingaddress", nativeQuery = true)
    String updateUserAddress(
        @Param("address") String addr,
        @Param("username") String uname
    );
}