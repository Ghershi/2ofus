package com.example.backendversion.dto;

import com.example.backendversion.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor

@Data
public class UserDTO {
    private int userId;
    private String username;
    private String userEmail;
    private String role;

    public int getUserId(UserEntity userEntity) {
        userId = userEntity.getUserId();
        return userId;
    }

    public UserDTO() { }



    public UserDTO(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.username = userEntity.getUsername();
        this.userEmail = userEntity.getUserEmail();
        this.role = userEntity.getRole();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}