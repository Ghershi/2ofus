package com.example.backendversion.repository;

import com.example.backendversion.entity.ActivityInviteEntity;
import com.example.backendversion.entity.ActivitySubmissionEntity;
import com.example.backendversion.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivitySubmissionRepository extends JpaRepository<ActivitySubmissionEntity, Integer> {
    int countByActivityInvite(ActivityInviteEntity activityInvite);

    Optional<List<ActivitySubmissionEntity>> findByActivityInvite(ActivityInviteEntity activityInvite);


}
