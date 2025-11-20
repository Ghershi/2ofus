package com.example.backendversion.dto;

public class EmotionRequest {
        private String type;


    public EmotionRequest() {
    }

    public EmotionRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
