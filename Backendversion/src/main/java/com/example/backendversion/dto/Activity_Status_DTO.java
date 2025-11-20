package com.example.backendversion.dto;

import com.example.backendversion.enums.Activity_Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data


public class Activity_Status_DTO {
    private Activity_Status activityStatus;

    public Activity_Status_DTO() {
    }

    public Activity_Status_DTO(Activity_Status activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Activity_Status getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Activity_Status activityStatus) {
        this.activityStatus = activityStatus;
    }
}
