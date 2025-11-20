package com.example.backendversion.dto;

import com.example.backendversion.entity.RequestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusDTO {
    private RequestEntity.Status requestStatus;

    public RequestEntity.Status getRequestStatus() {
        return requestStatus;
    }
    public void setRequestStatus(RequestEntity.Status requestStatus) {
        this.requestStatus = requestStatus;
    }
}
