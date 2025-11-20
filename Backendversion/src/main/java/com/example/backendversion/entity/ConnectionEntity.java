package com.example.backendversion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor

@Entity
@Table(name = "connection")
public class ConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private UserEntity user2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "last_interaction_at")
    private LocalDate lastInteractionAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mode mode;

    @Column(name = "countdown_date")
    private LocalDate countdownDate;

    private String lastEmotionTriggered;

    private LocalDateTime lastEmotionTriggeredAt;

    @OneToMany(mappedBy = "connectionEntity")
    private Set<ActivityInviteEntity>  activityInvites;

    @OneToMany(mappedBy = "connection", cascade = CascadeType.ALL)
    private Set<MessageEntity> messages = new HashSet<>();

    public enum Status {
        PENDING, ACCEPTED, BLOCKED
    }

    public enum Mode {
        COUPLE, BESTIE, FAMILY
    }

    public ConnectionEntity(RequestEntity requestEntity) {
        this.user1 = requestEntity.getSender();
        this.user2 = requestEntity.getReceiver();
        this.status = Status.ACCEPTED;
        this.createdAt = LocalDate.now();
        this.mode = Mode.COUPLE;
    }
    protected ConnectionEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser1() {
        return user1;
    }

    public void setUser1(UserEntity user1) {
        this.user1 = user1;
    }

    public UserEntity getUser2() {
        return user2;
    }

    public void setUser2(UserEntity user2) {
        this.user2 = user2;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getLastInteractionAt() {
        return lastInteractionAt;
    }

    public void setLastInteractionAt(LocalDate lastInteractionAt) {
        this.lastInteractionAt = lastInteractionAt;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public LocalDate getCountdownDate() {
        return countdownDate;
    }

    public void setCountdownDate(LocalDate countdownDate) {
        this.countdownDate = countdownDate;
    }

    public void setLastEmotionTriggered(String lastEmotionTriggered) {
        this.lastEmotionTriggered = lastEmotionTriggered;
    }

    public String getLastEmotionTriggered() {
        return lastEmotionTriggered;
    }
    public LocalDateTime getLastEmotionTriggeredAt() {

        return lastEmotionTriggeredAt;
    }
    public void setLastEmotionTriggeredAt(LocalDateTime lastEmotionTriggeredAt) {

        this.lastEmotionTriggeredAt = lastEmotionTriggeredAt;
    }

}