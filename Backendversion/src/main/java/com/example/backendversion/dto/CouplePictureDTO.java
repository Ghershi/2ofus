package com.example.backendversion.dto;

import java.time.LocalDateTime;

public class CouplePictureDTO {
    private int id;
    private String photoUrl;
    private LocalDateTime createdDate;
    private int userId;

    public CouplePictureDTO() {}

    public CouplePictureDTO(int id, String photoUrl, LocalDateTime createdDate, int userId) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.createdDate = createdDate;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
