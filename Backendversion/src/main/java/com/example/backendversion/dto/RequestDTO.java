package com.example.backendversion.dto;

import com.example.backendversion.entity.RequestEntity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDTO {

    private int requestId;
    private RequestEntity.Status status;
    private int senderId;
    private String senderUsername;
    private int receiverId;
    private String receiverUsername;
    private LocalDateTime createdAt;

    public RequestDTO() {}

    public RequestDTO(RequestEntity requestEntity) {
        this.requestId = requestEntity.getRequestId();
        this.status = requestEntity.getStatus();
        this.senderId = requestEntity.getSender().getUserId();
        this.receiverId = requestEntity.getReceiver().getUserId();
        this.senderUsername = requestEntity.getSender().getUsername();
        this.receiverUsername = requestEntity.getReceiver().getUsername();
        this.createdAt = requestEntity.getCreatedAt();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public RequestEntity.Status getStatus() {
        return status;
    }

    public void setStatus(RequestEntity.Status status) {
        this.status = status;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
