package com.example.backendversion.dto;

import java.time.LocalDate;

public class CountdownDateDTO {
    private LocalDate countdownDate;

    public LocalDate getCountdownDate() {
        return countdownDate;
    }

    public void setCountdownDate(LocalDate countdownDate) {
        this.countdownDate = countdownDate;
    }
}