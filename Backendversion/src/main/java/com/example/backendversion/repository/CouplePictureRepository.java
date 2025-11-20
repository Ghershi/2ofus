package com.example.backendversion.repository;

import com.example.backendversion.entity.CouplePictureEntity;
import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouplePictureRepository extends JpaRepository<CouplePictureEntity, Integer> {

    Optional<CouplePictureEntity> findFirstByUserOrderByCreatedDateDesc(UserEntity user);
}
