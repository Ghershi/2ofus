package com.example.backendversion.repository;

import com.example.backendversion.entity.ActivityInviteEntity;
import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ActivityInviteRepository extends JpaRepository<ActivityInviteEntity, Integer> {
    @Query("SELECT a FROM ActivityInviteEntity a " +
            "WHERE a.connectionEntity = :connection " +
            "AND a.initiatedBy = :connectedUser " +
            "AND a.status = 'PENDING'")
    Optional<List<ActivityInviteEntity>> findPendingActivityInvitesByConnectionAndUser(@Param("connection")ConnectionEntity connection, @Param("connectedUser") UserEntity connectedUser);

    @Query("SELECT a FROM ActivityInviteEntity a " +
            "WHERE a.connectionEntity = :connection " +
            "AND a.status = 'ACTIVE' " +
            "AND a.deadline > :now")
    Optional<List<ActivityInviteEntity>> findActiveActivityInvitesByConnection(@Param("connection")ConnectionEntity connection, @Param("now") LocalDateTime now);

    @Query("SELECT a FROM ActivityInviteEntity a " +
            "WHERE a.connectionEntity = :connection " +
            "AND a.status = 'COMPLETED'")
    Optional<List<ActivityInviteEntity>> findCompletedActivityInvitesByConnection(@Param("connection")ConnectionEntity connection);
}
