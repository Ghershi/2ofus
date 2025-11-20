package com.example.backendversion.controller;

import com.example.backendversion.dto.ConnectionDTO;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.service.ConnectionService;
import com.example.backendversion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/connection")
public class ConnectionController {
    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkConnection() {
        UserEntity currentUser = userService.getCurrentUser();
        boolean userIsConnected = connectionService.isUserConnected(currentUser);
        return ResponseEntity.ok(userIsConnected);
    }

    @GetMapping("/data")
    public ResponseEntity<?> getConnection() {
        UserEntity currentUser = userService.getCurrentUser();

        HashMap<String, Object> response = new HashMap<>();

        response.put("connectionData", connectionService.getConnectionData(currentUser));

        response.put("connectedUser", connectionService.getConnectedUser(currentUser));

        return ResponseEntity.ok(response);
    }


}
