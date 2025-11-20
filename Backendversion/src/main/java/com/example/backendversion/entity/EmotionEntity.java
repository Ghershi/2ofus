package com.example.backendversion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EmotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private LocalDateTime triggeredAt;

    @ManyToOne
    private UserEntity sender;

    @ManyToOne
    private UserEntity receiver;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private ConnectionEntity connectionEntity;


    public ConnectionEntity getConnectionEntity() {
        return connectionEntity;
    }

    public void setConnectionEntity(ConnectionEntity connectionEntity) {
        this.connectionEntity = connectionEntity;
    }

    public Long getId() { return id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }

    public UserEntity getSender() { return sender; }
    public void setSender(UserEntity sender) { this.sender = sender; }

    public UserEntity getReceiver() { return receiver; }
    public void setReceiver(UserEntity receiver) { this.receiver = receiver; }
}