package com.example.backendversion.dto;

import com.example.backendversion.entity.ActivitySubmissionEntity;

import java.time.LocalDateTime;

public class ActivitySubmissionDTO {
    private int activitySubmissionId;
    private int activityInvite;
    private UserDTO userDTO;
    private String caption;
    private String photoUrl;
    private LocalDateTime submittedAt;


    public ActivitySubmissionDTO() {}

    public ActivitySubmissionDTO(int activitySubmissionId, int activityInvite, UserDTO userDTO, String caption, String photoUrl, LocalDateTime submittedAt) {
        this.activitySubmissionId = activitySubmissionId;
        this.activityInvite = activityInvite;
        this.userDTO = userDTO;
        this.caption = caption;
        this.photoUrl = photoUrl;
        this.submittedAt = LocalDateTime.now();
    }

    public ActivitySubmissionDTO(ActivitySubmissionEntity entity) {
        this.activitySubmissionId = entity.getActivitySubmissionId();
        this.activityInvite = entity.getActivityInvite().getActivityInviteId();
        this.userDTO = new UserDTO(entity.getUserEntity());
        this.caption = entity.getCaption();
        this.photoUrl = entity.getPhotoUrl();
        this.submittedAt = entity.getSubmittedAt();
    }

    public int getActivitySubmissionId() {
        return activitySubmissionId;
    }

    public void setActivitySubmissionId(int activitySubmissionId) {
        this.activitySubmissionId = activitySubmissionId;
    }

    public int getActivityInvite() {
        return activityInvite;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}
