package com.example.backendversion.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "countdown_date_entity")  // Optional, nur wenn du den Tabellennamen explizit setzen willst
public class CountdownDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")  // FK-Spalte in der DB, referenziert UserEntity.userId
    private UserEntity user;

    private LocalDate countdownDate;

    // --- Konstruktoren ---
    public CountdownDateEntity() {}

    public CountdownDateEntity(UserEntity user, LocalDate countdownDate) {
        this.user = user;
        this.countdownDate = countdownDate;
    }

    // --- Getter & Setter ---
    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDate getCountdownDate() {
        return countdownDate;
    }

    public void setCountdownDate(LocalDate countdownDate) {
        this.countdownDate = countdownDate;
    }

}