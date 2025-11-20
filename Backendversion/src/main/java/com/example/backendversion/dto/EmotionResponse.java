package com.example.backendversion.dto;

public class EmotionResponse {
    private String triggeredAt;

    public EmotionResponse() {}

    public EmotionResponse(String triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public String getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(String triggeredAt) {
        this.triggeredAt = triggeredAt;
    }
}
