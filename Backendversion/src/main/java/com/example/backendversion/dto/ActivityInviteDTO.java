package com.example.backendversion.dto;
import com.example.backendversion.entity.ActivityInviteEntity;
import com.example.backendversion.enums.Activity_Status;

import java.time.LocalDateTime;

public class ActivityInviteDTO {

    private int activityInviteId;
    private int activityId;
    private long connection;
    private int initiatedBy;
    private Activity_Status status;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private LocalDateTime completedAt;
    private String comment;

    public ActivityInviteDTO() {}

    public ActivityInviteDTO(int activityInviteId, int activityId, long connection, int initiatedBy, Activity_Status status, LocalDateTime startTime, LocalDateTime deadline, String comment) {
        this.activityInviteId = activityInviteId;
        this.activityId = activityId;
        this.connection = connection;
        this.initiatedBy = initiatedBy;
        this.status = status;
        this.startTime = startTime;
        this.deadline = startTime.plusHours(24);
        this.comment = comment;
    }

    public ActivityInviteDTO(int activityId, long connection, int initiatedBy, String comment, Activity_Status activityStatus) {
        this.activityId = activityId;
        this.connection = connection;
        this.initiatedBy = initiatedBy;
        this.comment = comment;
        this.status = activityStatus;
    }

    public ActivityInviteDTO(ActivityInviteEntity activityInviteEntity) {
        this.activityId = activityInviteEntity.getActivityId();
        this.activityInviteId = activityInviteEntity.getActivityInviteId();
        this.comment = activityInviteEntity.getComment();
    }

    public ActivityInviteDTO(int activityInviteId, int activityId, LocalDateTime deadline) {
        this.activityId = activityId;
        this.activityInviteId = activityInviteId;
        this.deadline = deadline;
    }

    public ActivityInviteDTO(int activityInviteId, int activityId) {
        this.activityId = activityId;
        this.activityInviteId = activityInviteId;
    }

    public int getActivityInviteId() {
        return activityInviteId;
    }

    public void setActivityInviteId(int activityInviteId) {
        this.activityInviteId = activityInviteId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public long getConnection() {
        return connection;
    }

    public void setConnection(long connection) {this.connection = connection;}

    public int getInitiatedBy() {return initiatedBy;}

    public void setInitiatedBy(int initiatedBy) {
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
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
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
