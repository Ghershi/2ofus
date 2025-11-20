package com.example.backendversion.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public class EmotionStatusDTO {
        private String type;
        private LocalDateTime triggeredAt;

        public EmotionStatusDTO() {
                // Default-Konstruktor
        }

        public EmotionStatusDTO(String type, LocalDateTime triggeredAt) {
                this.type = type;
                this.triggeredAt = triggeredAt;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public LocalDateTime getTriggeredAt() {
                return triggeredAt;
        }

        public void setTriggeredAt(LocalDateTime triggeredAt) {
                this.triggeredAt = triggeredAt;
        }}


