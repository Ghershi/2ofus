package com.example.backendversion.controller;

import com.example.backendversion.dto.RequestStatusDTO;
import com.example.backendversion.dto.UserDTO;
import com.example.backendversion.entity.RequestEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.service.ConnectionService;
import com.example.backendversion.service.RequestService;
import com.example.backendversion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/invitation")
public class RequestController {
    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ConnectionService connectionService;


    @PostMapping("/send")
    public ResponseEntity<?> sendRequest(@RequestParam int receiverId) {
        UserEntity sender = userService.getCurrentUser();
        UserEntity receiver = userService.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User nicht gefunden"));
        boolean isReceiverConnected = connectionService.isUserConnected(receiver);
        boolean requestWithSameSenderAndReceiverExists = requestService.requestWithSameSenderAndReceiverExists(sender, receiver);
        if (isReceiverConnected) {
            return ResponseEntity.badRequest().body(Map.of("error", receiver.getUsername() + " ist in einer Verbindung"));
        } else if (requestWithSameSenderAndReceiverExists) {
            return ResponseEntity.badRequest().body(Map.of("error", "Du hast schon eine Einladung an " + receiver.getUsername() + " gesendet"));
        } else {
            requestService.createRequest(sender, receiver);
        }
        return ResponseEntity.ok(Map.of("message", "Deine Einladung wurde an " + receiver.getUsername() + " gesendet."));
    }

    @PostMapping
    public ResponseEntity<Void> respondToRequest(
            @RequestParam int requestId,
            @RequestBody RequestStatusDTO dto ) {
        if(dto.getRequestStatus() == RequestEntity.Status.ACCEPTED){
            requestService.acceptRequest(requestId);
            return ResponseEntity.ok().build();
        }else if (dto.getRequestStatus() == RequestEntity.Status.DECLINED){
            requestService.declineRequest(requestId);
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/listAll")
    public ResponseEntity<?> listAllRequests() {
        UserEntity currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(requestService.getAllRequests(currentUser));
    }

}