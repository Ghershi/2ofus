package com.example.backendversion.entity;

import com.example.backendversion.enums.Activity_Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class ActivityInviteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int activityInviteId;

    private int activityId;

    @ManyToOne
    @JoinColumn(name = "connection_id", nullable = false)
    private ConnectionEntity connectionEntity;

    @ManyToOne
    @JoinColumn(name = "initiated_by", nullable = false)
    private UserEntity initiatedBy;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Activity_Status status;

    private String comment;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    private LocalDateTime deadline;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "activityInvite")
    private Set<ActivitySubmissionEntity> activitySubmission;

    protected ActivityInviteEntity() {
    }

    public ActivityInviteEntity(int activityId, ConnectionEntity connectionEntity, UserEntity userEntity, Activity_Status status, String comment, LocalDateTime startTime, LocalDateTime deadline, LocalDateTime completedAt) {
        this.connectionEntity = connectionEntity;
        this.initiatedBy = userEntity;
        this.status = Activity_Status.PENDING;
        this.comment = comment;
        this.startTime = startTime;
        this.deadline = deadline;
        this.completedAt = completedAt;
        this.activityId = activityId;
        this.activityInviteId = activityId;
    }

    public ActivityInviteEntity(int activityId, ConnectionEntity connection, UserEntity initiatedBy, String comment) {
        this.activityId = activityId;
        this.connectionEntity = connection;
        this.initiatedBy = initiatedBy;
        this.comment = comment;
        this.status = Activity_Status.PENDING;
    }

    public int getActivityInviteId() {
        return activityInviteId;
    }

    public void setActivityInviteId(int activityId){
        this.activityInviteId = activityId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public ConnectionEntity getConnectionEntity() {
        return connectionEntity;
    }

    public void setConnectionEntity(ConnectionEntity connectionEntity) {
        this.connectionEntity = connectionEntity;
    }

    public UserEntity getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(UserEntity initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public Activity_Status getStatus() {
        return status;
    }

    public void setStatus(Activity_Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        this.deadline = startTime.plusHours(24);
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }













}
