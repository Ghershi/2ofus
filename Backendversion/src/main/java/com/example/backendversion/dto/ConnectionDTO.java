package com.example.backendversion.dto;

import com.example.backendversion.entity.ConnectionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor

public class ConnectionDTO {
    private long id;
    private int user1;
    private int user2;
    private ConnectionEntity.Status status;
    private LocalDate createdAt;
    private ConnectionEntity.Mode mode;
    private LocalDate countdownDate;

    private String lastEmotionTriggered;
    private LocalDateTime lastEmotionTriggeredAt;


    public ConnectionDTO(ConnectionEntity connectionEntity) {
        this.id = connectionEntity.getId();
        this.user1 = connectionEntity.getUser1().getUserId() ;
        this.user2 = connectionEntity.getUser2().getUserId() ;
        this.status = connectionEntity.getStatus();
        this.createdAt = connectionEntity.getCreatedAt();
        this.mode = connectionEntity.getMode();
        this.countdownDate = connectionEntity.getCountdownDate();
        this.lastEmotionTriggered = connectionEntity.getLastEmotionTriggered();
        this.lastEmotionTriggeredAt = connectionEntity.getLastEmotionTriggeredAt();

    }
    public ConnectionDTO() {

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public ConnectionEntity.Status getStatus() {
        return status;
    }

    public void setStatus(ConnectionEntity.Status status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public ConnectionEntity.Mode getMode() {
        return mode;
    }

    public void setMode(ConnectionEntity.Mode mode) {
        this.mode = mode;
    }

    public LocalDate getCountdownDate() {
        return countdownDate;
    }

    public void setCountdownDate(LocalDate countdownDate) {
        this.countdownDate = countdownDate;
    }
    public String getLastEmotionTriggered() {
        return lastEmotionTriggered;
    }

    public void setLastEmotionTriggered(String lastEmotionTriggered) {
        this.lastEmotionTriggered = lastEmotionTriggered;
    }

    public LocalDateTime getLastEmotionTriggeredAt() {
        return lastEmotionTriggeredAt;
    }

    public void setLastEmotionTriggeredAt(LocalDateTime lastEmotionTriggeredAt) {
        this.lastEmotionTriggeredAt = lastEmotionTriggeredAt;
    }
}
