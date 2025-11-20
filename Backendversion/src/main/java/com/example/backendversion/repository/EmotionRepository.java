package com.example.backendversion.repository;

import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.EmotionEntity;
import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<EmotionEntity, Long> {

    int countByConnectionEntityAndSenderAndReceiverAndType(
            ConnectionEntity connection,
            UserEntity sender,
            UserEntity receiver,
            String type
    );

}
