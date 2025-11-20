package com.example.backendversion.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_submission")
public class ActivitySubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int activitySubmissionId;

    @ManyToOne
    @JoinColumn(name = "activity_invite_id")
    private ActivityInviteEntity activityInvite;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false)
    private String caption;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    protected ActivitySubmissionEntity() {
    }

    public ActivitySubmissionEntity(ActivityInviteEntity activityInviteEntity, UserEntity userEntity, String caption, String photo_url, LocalDateTime submittedAt) {
        this.activityInvite = activityInviteEntity;
        this.userEntity = userEntity;
        this.caption = caption;
        this.photoUrl = photo_url;
        this.submittedAt = LocalDateTime.now();
    }

    public int getActivitySubmissionId() {
        return activitySubmissionId;
    }

    public void setActivitySubmissionId(int activitySubmissionId) {
        this.activitySubmissionId = activitySubmissionId;
    }

    public ActivityInviteEntity getActivityInvite() {
        return activityInvite;
    }

    public void setActivityInvite(ActivityInviteEntity activityInvite) {
        this.activityInvite = activityInvite;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
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

    public void setPhotoUrl(String photo_url) {
        this.photoUrl = photo_url;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }









}
