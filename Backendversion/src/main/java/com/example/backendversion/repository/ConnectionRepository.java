package com.example.backendversion.repository;

import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
    List<ConnectionEntity> findByUser1OrUser2(UserEntity user1, UserEntity user2);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ConnectionEntity c " +
            "WHERE (c.user1 = :user OR c.user2 = :user) AND c.status = 'ACCEPTED'")
    boolean existsAcceptedConnectionByUser(@Param("user") UserEntity user);

    @Query("SELECT c FROM ConnectionEntity c " +
            "WHERE (c.user1 = :user OR c.user2 = :user) AND c.status = 'ACCEPTED'")
    ConnectionEntity findAcceptedConnectionByUser(@Param("user") UserEntity user);

    @Query("SELECT c FROM ConnectionEntity c " +
            "WHERE (c.user1 = :user OR c.user2 = :user) AND c.status = 'ACCEPTED'")
    List<ConnectionEntity> findAcceptedConnectionsByUser(@Param("user") UserEntity user);

}


