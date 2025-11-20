package com.example.backendversion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data

public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private LocalDateTime timestamp;
    private String imageUrl;

    @ManyToOne
    private UserEntity sender;

    @ManyToOne
    private ConnectionEntity connection;



    public MessageEntity() {
    }

    public MessageEntity(Long id, String text, LocalDateTime timestamp, String imageUrl, UserEntity sender, ConnectionEntity connection) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.sender = sender;
        this.connection = connection;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserEntity getSender() {
        return sender;
    }
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public ConnectionEntity getConnection() {
        return connection;
    }
    public void setConnection(ConnectionEntity connection) {
        this.connection = connection;
    }

}