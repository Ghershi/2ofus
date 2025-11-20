package com.example.backendversion.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CouplePictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String photoUrl;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    public CouplePictureEntity() {
    }

    public CouplePictureEntity(String photoUrl, LocalDateTime createdDate, UserEntity user) {
        this.photoUrl = photoUrl;
        this.createdDate = createdDate;
        this.user = user;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }




}
