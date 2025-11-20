package com.example.backendversion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Entity
    @Table(name = "users")
    public class UserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int userId;

        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false)
        private String userPassword;

        @Column(nullable = false, unique = true)
        private String userEmail;

        @Column(nullable = false)
        private int userAge;

        @Column(nullable = false)
        private String role = "user";

        @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
        private List<RequestEntity> sentRequests;

        @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
        private List<RequestEntity> receivedRequests;

        @OneToMany(mappedBy = "user1")
        private Set<ConnectionEntity> connectionsInitiated;

        @OneToMany(mappedBy = "user2")
        private Set<ConnectionEntity> connectionsReceived;

        @OneToMany(mappedBy = "initiatedBy")
        private Set<ActivityInviteEntity> activityInvites;

        @OneToMany(mappedBy = "userEntity")
        private Set<ActivitySubmissionEntity> activitySubmissions;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessageEntity> messagesSent = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CountdownDateEntity> countdownDates = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private Set<CouplePictureEntity> couplePictures = new HashSet<>();


    public UserEntity() {

    }

    public UserEntity(int userId, String username, String userPassword, String userEmail, int userAge, String role) {
        this.userId = userId;
        this.username = username;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userAge = userAge;
        this.role = role;
    }

    public List<CountdownDateEntity> getCountdownDates() {
        return countdownDates;
    }
    public void setCountdownDates(List<CountdownDateEntity> countdownDates) {
        this.countdownDates = countdownDates;
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<RequestEntity> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<RequestEntity> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<RequestEntity> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<RequestEntity> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public Set<ConnectionEntity> getConnectionsInitiated() {
        return connectionsInitiated;
    }

    public void setConnectionsInitiated(Set<ConnectionEntity> connectionsInitiated) {
        this.connectionsInitiated = connectionsInitiated;
    }

    public Set<ConnectionEntity> getConnectionsReceived() {
        return connectionsReceived;
    }

    public void setConnectionsReceived(Set<ConnectionEntity> connectionsReceived) {
        this.connectionsReceived = connectionsReceived;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userAge=" + userAge +
                ", role='" + role + '\'' +
                '}';
    }


}
