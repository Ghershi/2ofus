package com.example.backendversion.repository;

import com.example.backendversion.entity.RequestEntity;
import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {

    List<RequestEntity> findByReceiverAndStatus(UserEntity receiver, RequestEntity.Status status);

    List<RequestEntity> findBySenderAndStatus(UserEntity sender, RequestEntity.Status status);

    List<RequestEntity> findBySender(UserEntity sender);

    @Query("SELECT r FROM RequestEntity r WHERE r.sender = :sender AND r.receiver = :receiver")
    Optional<RequestEntity> findBySenderAndReceiver(
            @Param("sender") UserEntity sender,
            @Param("receiver") UserEntity receiver
    );


}