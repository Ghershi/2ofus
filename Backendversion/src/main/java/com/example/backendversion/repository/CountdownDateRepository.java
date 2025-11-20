package com.example.backendversion.repository;

import com.example.backendversion.entity.CountdownDateEntity;
import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountdownDateRepository extends JpaRepository<CountdownDateEntity, Long> {
    Optional<CountdownDateEntity> findTopByUserUserIdOrderByIdDesc(int userId);;
    List<CountdownDateEntity> findByUser(UserEntity user);
}