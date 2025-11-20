package com.example.backendversion.controller;


import com.example.backendversion.dto.CountdownDateDTO;
import com.example.backendversion.dto.EmotionRequest;
import com.example.backendversion.dto.EmotionResponse;
import com.example.backendversion.dto.EmotionStatusDTO;
import com.example.backendversion.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/countdown")
public class EmotionController {
    @Autowired
    ConnectionService connectionService;



   /* @PostMapping("/emotions/trigger")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> triggerEmotion() {
        System.out.println("Authorized access!");
        return ResponseEntity.ok().build();
    }*/
   /*@PostMapping("/emotions/trigger")
   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<?> triggerEmotion(@RequestBody EmotionRequest request) {
       try {
           System.out.println("Received emotion: " + request.getType());
           connectionService.triggerEmotion(request.getType());
           return ResponseEntity.ok().build();
       } catch (Exception e) {
           e.printStackTrace(); // oder Logger verwenden
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Verarbeiten");
       }

   }*/

    @PostMapping("/emotions/trigger")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmotionResponse> triggerEmotion(@RequestBody EmotionRequest request) {
        String triggeredAt = connectionService.triggerEmotion(request.getType());

        EmotionResponse response = new EmotionResponse(triggeredAt);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/emotions/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmotionStatusDTO> getEmotionStatus() {
        return ResponseEntity.ok(connectionService.getEmotionStatus());
    }

}
