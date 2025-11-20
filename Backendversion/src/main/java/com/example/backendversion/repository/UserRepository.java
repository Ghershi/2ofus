package com.example.backendversion.repository;

import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUserEmail(String userEmail);
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUserEmail(String email);
}